import javax.swing.*;
import java.awt.*;

/**
 * UI相关
 *
 * @author YTMartian
 */

public class UI {

    /**
     * 主窗口位置和大小
     */
    public final static int MAIN_WINDOW_X = 240;
    public final static int MAIN_WINDOW_Y = 100;
    public final static int MAIN_WINDOW_WIDTH = 800;
    public final static int MAIN_WINDOW_HEIGHT = 600;
    /**
     * 系统当前路径
     */
    public final static String CURRENT_DIR = System.getProperty("user.dir");

    /**
     * 窗口图标
     */
    public final static Image IMAGE_ICON = Toolkit.getDefaultToolkit()
            .getImage(MainInterface.class.getResource("images/icon.png"));

    /**
     * 按钮图标
     */
    // 选择源目录或文件按钮 默认
    public final static ImageIcon ICON_BUTTON1 = new ImageIcon(
            MainInterface.class.getResource("images/button1.png"));
    // 选择源目录或文件按钮 激活
    public final static ImageIcon ICON_BUTTON1_ENABLE = new ImageIcon(
            MainInterface.class.getResource("images/button1Enable.png"));
    // 选择源目录或文件按钮 失效
    public final static ImageIcon ICON_BUTTON1_DISABLE = new ImageIcon(
            MainInterface.class.getResource("images/button1Disable.png"));
    // 选择目标目录 默认
    public final static ImageIcon ICON_BUTTON2 = new ImageIcon(
            MainInterface.class.getResource("images/button2.png"));
    // 选择目标目录 激活
    public final static ImageIcon ICON_BUTTON2_ENABLE = new ImageIcon(
            MainInterface.class.getResource("images/button2Enable.png"));
    // 选择目标目录 失效
    public final static ImageIcon ICON_BUTTON2_DISABLE = new ImageIcon(
            MainInterface.class.getResource("images/button2Disable.png"));
    // 开始 默认
    public final static ImageIcon ICON_START_BUTTON = new ImageIcon(
            MainInterface.class.getResource("images/start.png"));
    // 开始 激活
    public final static ImageIcon ICON_START_BUTTON_ENABLE = new ImageIcon(
            MainInterface.class.getResource("images/startEnable.png"));
    // 开始 失效
    public final static ImageIcon ICON_START_BUTTON_DISABLE = new ImageIcon(
            MainInterface.class.getResource("images/startDisable.png"));
    // 暂停 默认
    public final static ImageIcon ICON_PAUSE_BUTTON = new ImageIcon(
            MainInterface.class.getResource("images/pause.png"));
    // 暂停 激活
    public final static ImageIcon ICON_PAUSE_BUTTON_ENABLE = new ImageIcon(
            MainInterface.class.getResource("images/pauseEnable.png"));
    // 暂停 失效
    public final static ImageIcon ICON_PAUSE_BUTTON_DISABLE = new ImageIcon(
            MainInterface.class.getResource("images/pauseDisable.png"));
    // 取消 默认
    public final static ImageIcon ICON_CANCEL_BUTTON = new ImageIcon(
            MainInterface.class.getResource("images/cancel.png"));
    // 取消 激活
    public final static ImageIcon ICON_CANCEL_BUTTON_ENABLE = new ImageIcon(
            MainInterface.class.getResource("images/cancelEnable.png"));
    // 取消 失效
    public final static ImageIcon ICON_CANCEL_BUTTON_DISABLE = new ImageIcon(
            MainInterface.class.getResource("images/cancelDisable.png"));
    // 继续 默认
    public final static ImageIcon ICON_CONTINUE_BUTTON = new ImageIcon(
            MainInterface.class.getResource("images/continue.png"));
    // 继续 激活
    public final static ImageIcon ICON_CONTINUE_BUTTON_ENABLE = new ImageIcon(
            MainInterface.class.getResource("images/continueEnable.png"));
    // 继续 失效
    public final static ImageIcon ICON_CONTINUE_BUTTON_DISABLE = new ImageIcon(
            MainInterface.class.getResource("images/continueDisable.png"));

}
