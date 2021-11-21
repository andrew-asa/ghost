package com.asa.base.ui.jfxsupport;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author andrew_asa
 * @date 2021/11/21.
 */
@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface FXMLView {

    /**
     * fxml 文件路径
     * @return
     */
    String value() default "";

    /**
     * 当前fxml样式文件
     */
    String[] css() default {};

    /**
     * 当前fxml文件资源文件
     */
    String bundle() default "";

    /**
     * 当前模型标题
     */
    String title() default "";

    /**
     * The style to be applied to the underlying stage
     * when using this view as a modal window.
     */
    String stageStyle() default "UTILITY";
}
