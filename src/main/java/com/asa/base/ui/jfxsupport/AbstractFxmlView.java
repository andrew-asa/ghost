package com.asa.base.ui.jfxsupport;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static java.util.ResourceBundle.getBundle;

/**
 * @author andrew_asa
 * @date 2021/11/21.
 */
public abstract class AbstractFxmlView implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFxmlView.class);

    private final ObjectProperty<Object> presenterProperty;

    private final Optional<ResourceBundle> bundle;

    private final URL resource;

    private final FXMLView annotation;

    private FXMLLoader fxmlLoader;

    private ApplicationContext applicationContext;

    private String fxmlRoot;

    public AbstractFxmlView() {
        LOGGER.debug("AbstractFxmlView construction");
        final String filePathFromPackageName = PropertyReaderHelper.determineFilePathFromPackageName(getClass());
        setFxmlRootPath(filePathFromPackageName);
        annotation = getFXMLAnnotation();
        resource = getURLResource(annotation);
        presenterProperty = new SimpleObjectProperty<>();
        bundle = getResourceBundle(getBundleName());
    }

    /**
     * 获取当前view代表的fxml文件资源
     */
    private URL getURLResource(final FXMLView annotation) {
        if (annotation != null && !annotation.value().equals("")) {
            return getClass().getResource(annotation.value());
        } else {
            return getClass().getResource(getFxmlPath());
        }
    }

    /**
     * 获取注解
     *
     * @return the FXML annotation
     */
    private FXMLView getFXMLAnnotation() {
        final Class<? extends AbstractFxmlView> theClass = this.getClass();
        final FXMLView annotation = theClass.getAnnotation(FXMLView.class);
        return annotation;
    }

    /**
     * 获取fxml文件关联的controller
     */
    private Object createControllerForType(final Class<?> type) {
        return applicationContext.getBean(type);
    }

    /*
     * 保存容器上下文
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        if (this.applicationContext != null) {
            return;
        }
        this.applicationContext = applicationContext;
    }

    private void setFxmlRootPath(final String path) {
        fxmlRoot = path;
    }

    /**
     * 异步加载
     * 主要是定制FXMLLoader的controller工厂类，并加载fxml文件
     */
    private FXMLLoader loadSynchronously(final URL resource, final Optional<ResourceBundle> bundle) throws IllegalStateException {

        final FXMLLoader loader = new FXMLLoader(resource, bundle.orElse(null));
        loader.setControllerFactory(this::createControllerForType);
        try {
            loader.load();
        } catch (final IOException | IllegalStateException e) {
            throw new IllegalStateException("Cannot load " + getConventionalName(), e);
        }
        return loader;
    }

    /**
     * 确保已经从fxml文件转化成为node
     */
    private void ensureFxmlLoaderInitialized() {

        if (fxmlLoader != null) {
            return;
        }
        fxmlLoader = loadSynchronously(resource, bundle);
        presenterProperty.set(fxmlLoader.getController());
    }

    /**
     * 提供显示node
     */
    public Parent getView() {

        ensureFxmlLoaderInitialized();
        final Parent parent = fxmlLoader.getRoot();
        addCSSIfAvailable(parent);
        return parent;
    }

    /**
     * Initializes the view synchronously and invokes the consumer with the
     * created parent Node within the FX UI thread.
     *
     * @param consumer
     *            - an object interested in received the {@link Parent} as
     *            callback
     */
    public void getView(final Consumer<Parent> consumer) {
        CompletableFuture.supplyAsync(this::getView, Platform::runLater).thenAccept(consumer);
    }

    /**
     * 获取第一个子元素
     */
    public Node getViewWithoutRootContainer() {

        final ObservableList<Node> children = getView().getChildrenUnmodifiable();
        if (children.isEmpty()) {
            return null;
        }
        return children.listIterator().next();
    }

    /**
     * node添加定制的css样式
     */
    void addCSSIfAvailable(final Parent parent) {

        // Read global css when available:
        final List<String> list = PropertyReaderHelper.get(applicationContext.getEnvironment(), "javafx.css");
        if (!list.isEmpty()) {
            list.forEach(css -> parent.getStylesheets().add(getClass().getResource(css).toExternalForm()));
        }
        addCSSFromAnnotation(parent);
        final URL uri = getClass().getResource(getStyleSheetName());
        if (uri == null) {
            return;
        }
        final String uriToCss = uri.toExternalForm();
        parent.getStylesheets().add(uriToCss);
    }

    /**
     * 添加注解中的css文件到node中
     */
    private void addCSSFromAnnotation(final Parent parent) {
        if (annotation != null && annotation.css().length > 0) {
            for (final String cssFile : annotation.css()) {
                final URL uri = getClass().getResource(cssFile);
                if (uri != null) {
                    final String uriToCss = uri.toExternalForm();
                    parent.getStylesheets().add(uriToCss);
                    LOGGER.debug("css file added to parent: {}", cssFile);
                } else {
                    LOGGER.warn("referenced {} css file could not be located", cssFile);
                }
            }
        }
    }

    /*
     * 获取默认标题
     *
     */
    String getDefaultTitle() {
        return annotation.title();
    }

    /*
     * 窗口样式
     */
    StageStyle getDefaultStyle() {
        final String style = annotation.stageStyle();
        return StageStyle.valueOf(style.toUpperCase());
    }

    /**
     * Gets the style sheet name.
     *
     * @return the style sheet name
     */
    private String getStyleSheetName() {
        return fxmlRoot + getConventionalName(".css");
    }

    /**
     * In case the view was not initialized yet, the conventional fxml
     * (airhacks.fxml for the AirhacksView and AirhacksPresenter) are loaded and
     * the specified presenter / controller is going to be constructed and
     * returned.
     *
     * @return the corresponding controller / presenter (usually for a
     *         AirhacksView the AirhacksPresenter)
     */
    public Object getPresenter() {

        ensureFxmlLoaderInitialized();

        return presenterProperty.get();
    }

    /**
     * Does not initialize the view. Only registers the Consumer and waits until
     * the the view is going to be created / the method FXMLView#getView or
     * FXMLView#getViewAsync invoked.
     *
     * @param presenterConsumer
     *            listener for the presenter construction
     */
    public void getPresenter(final Consumer<Object> presenterConsumer) {

        presenterProperty.addListener(
                (final ObservableValue<? extends Object> o, final Object oldValue, final Object newValue) -> {
                    presenterConsumer.accept(newValue);
                });
    }

    /**
     * Gets the conventional name.
     *
     * @param ending
     *            the suffix to append
     * @return the conventional name with stripped ending
     */
    private String getConventionalName(final String ending) {
        return getConventionalName() + ending;
    }

    /**
     * Gets the conventional name.
     *
     * @return the name of the view without the "View" prefix in lowerCase. For
     *         AirhacksView just airhacks is going to be returned.
     */
    private String getConventionalName() {
        return stripEnding(getClass().getSimpleName().toLowerCase());
    }

    /**
     * Gets the bundle name.
     *
     * @return the bundle name
     */
    private String getBundleName() {
        if (StringUtils.isEmpty(annotation.bundle())) {
            final String lbundle = getClass().getPackage().getName() + "." + getConventionalName();
            LOGGER.debug("Bundle: {} based on conventional name.", lbundle);
            return lbundle;
        }

        final String lbundle = annotation.bundle();
        LOGGER.debug("Annotated bundle: {}", lbundle);
        return lbundle;
    }

    /**
     * Strip ending.
     *
     * @param clazz
     *            the clazz
     * @return the string
     */
    private static String stripEnding(final String clazz) {

        if (!clazz.endsWith("view")) {
            return clazz;
        }

        return clazz.substring(0, clazz.lastIndexOf("view"));
    }

    /**
     * Gets the fxml file path.
     *
     * @return the relative path to the fxml file derived from the FXML view.
     *         e.g. The name for the AirhacksView is going to be
     *         <PATH>/airhacks.fxml.
     */

    final String getFxmlPath() {
        final String fxmlPath = fxmlRoot + getConventionalName(".fxml");
        LOGGER.debug("Determined fxmlPath: " + fxmlPath);
        return fxmlPath;
    }

    /**
     * Returns a resource bundle if available
     *
     * @param name
     *            the name of the resource bundle.
     * @return the resource bundle
     */
    private Optional<ResourceBundle> getResourceBundle(final String name) {
        ResourceBundle bundle;
        try {
            LOGGER.debug("Resource bundle: " + name);
            bundle = getBundle(name);
        } catch (final MissingResourceException ex) {
            LOGGER.debug("No resource bundle could be determined: " + ex.getMessage());
            bundle = null;
        }

        return Optional.ofNullable(bundle);
    }

    /**
     * Gets the resource bundle.
     *
     * @return an existing resource bundle, or null
     */
    public Optional<ResourceBundle> getResourceBundle() {
        return bundle;
    }

    @Override
    public String toString() {
        return "AbstractFxmlView [presenterProperty=" + presenterProperty + ", bundle=" + bundle + ", resource="
                + resource + ", fxmlRoot=" + fxmlRoot + "]";
    }

}
