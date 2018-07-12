import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class MainInterface {
    /**
     * 源文件或目录和目标文件和目录
     */
    private static String sourcePath = "";
    private static String destinationPath = "";
    private static long sourceSize = 0;//源文件大小
    private static long destinationSize = 0;//目标文件大小
    private static Thread thread;
    private static boolean isPause = false;
    private static boolean isCancel = false;
    private static Point p;//用以控制scroll到最底部

    private JPanel panel1;
    private JButton button1;
    private JButton button2;
    private JButton startButton;
    private JButton pauseButton;
    private JButton cancelButton;
    private JProgressBar progressBar1;
    private JTextArea textArea1;
    private JScrollPane scroll;


    private MainInterface() {

        setButtons();
        setTextArea();
        setProgressBar();
        p = new Point();

        /**
         * 监听按钮 1,获取源文件或目录
         */
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * 文件选择窗口
                 */
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle("选择目录或文件");
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                JFrame frame = new JFrame("选择目录或文件");
                frame.setIconImage(UI.IMAGE_ICON);
                int result = jfc.showOpenDialog(frame);
                if (result == 1) return;//若未选择，则result == 1
                File file = jfc.getSelectedFile();

                sourcePath = file.getAbsolutePath();
                sourceSize = getSize(new File(sourcePath)) + 1;//防止除0错误
                textArea1.setText(textArea1.getText() + "源文件或目录：" + sourcePath + "\n");
            }
        });
        /**
         * 监听按钮 2,获取目的目录
         */
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setDialogTitle("选择目录");
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只显示目录，因为目标必须要是文件
                JFrame frame = new JFrame("选择目录或文件");
                frame.setIconImage(UI.IMAGE_ICON);
                int result = jfc.showOpenDialog(frame);
                if (result == 1) return;//若未选择，则result == 1
                File file = jfc.getSelectedFile();
                destinationPath = file.getAbsolutePath();
                destinationSize = 1;
                textArea1.setText(textArea1.getText() + "目标目录：" + destinationPath + "\n");
            }
        });
        /**
         * 监听开始按钮
         */
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sourcePath.isEmpty() || destinationPath.isEmpty()) {
                    Toolkit.getDefaultToolkit().beep();//发出警告声
                    JOptionPane.showMessageDialog(null, "未选择源或目标路径!", "警告", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (sourcePath.equals(destinationPath)) return;
                progressBar1.setValue(0);
                destinationSize = 1;
                isCancel = false;
                textArea1.setText(textArea1.getText() + "开始传输  \n");
                thread = new MyThread();
                thread.start();
            }
        });
        /**
         * 监听暂停按钮
         */
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((int) ((double) destinationSize / (double) sourceSize * 100) == 100) return;
                isPause = !isPause;
            }
        });
        /**
         * 监听取消按钮
         */
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sourcePath.isEmpty() || destinationPath.isEmpty()) return;
                //thread.stop();
                textArea1.setText(textArea1.getText() + "传输取消  \n");
                scrollToBottom();
                progressBar1.setValue(0);
                progressBar1.setString("0%");
                isPause = false;
                isCancel = true;
            }
        });
    }

    /**
     * @param args main方法参数
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("本机目录及文件的操作");
        frame.setContentPane(new MainInterface().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        /**
         * 设置窗口位置及大小
         */
        frame.setBounds(UI.MAIN_WINDOW_X, UI.MAIN_WINDOW_Y, UI.MAIN_WINDOW_WIDTH,
                UI.MAIN_WINDOW_HEIGHT);
        frame.setResizable(false);//窗口大小不可调节
        /**
         * 设置图标
         */
        frame.setIconImage(UI.IMAGE_ICON);
        /**
         * 窗口可见
         */
        frame.setVisible(true);

    }

    /**
     * 设置按钮的样式，包括激活，悬停，失效的图标
     */
    private void setButtons() {
        /**
         * 设置button1的样式
         */
        button1.setBorderPainted(false);
        button1.setFocusPainted(false);
        button1.setContentAreaFilled(false);
        button1.setFocusable(true);
        button1.setMargin(new Insets(0, 0, 0, 0));

        button1.setRolloverIcon(UI.ICON_BUTTON1);
        button1.setPressedIcon(UI.ICON_BUTTON1_ENABLE);
        button1.setDisabledIcon(UI.ICON_BUTTON1_DISABLE);

        /**
         * 设置button2的样式
         */
        button2.setBorderPainted(false);
        button2.setFocusPainted(false);
        button2.setContentAreaFilled(false);
        button2.setFocusable(true);
        button2.setMargin(new Insets(0, 0, 0, 0));

        button2.setRolloverIcon(UI.ICON_BUTTON2);
        button2.setPressedIcon(UI.ICON_BUTTON2_ENABLE);
        button2.setDisabledIcon(UI.ICON_BUTTON2_DISABLE);
        /**
         * 设置开始按钮的样式
         */
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusable(true);
        startButton.setMargin(new Insets(0, 0, 0, 0));

        startButton.setRolloverIcon(UI.ICON_START_BUTTON);
        startButton.setPressedIcon(UI.ICON_START_BUTTON_ENABLE);
        startButton.setDisabledIcon(UI.ICON_START_BUTTON_DISABLE);
        /**
         * 设置暂停按钮的样式
         */
        pauseButton.setBorderPainted(false);
        pauseButton.setFocusPainted(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setFocusable(true);
        pauseButton.setMargin(new Insets(0, 0, 0, 0));

        pauseButton.setRolloverIcon(UI.ICON_PAUSE_BUTTON);
        pauseButton.setPressedIcon(UI.ICON_PAUSE_BUTTON_ENABLE);
        pauseButton.setDisabledIcon(UI.ICON_PAUSE_BUTTON_DISABLE);
        /**
         * 设置取消按钮的样式
         */
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setContentAreaFilled(false);
        cancelButton.setFocusable(true);
        cancelButton.setMargin(new Insets(0, 0, 0, 0));

        cancelButton.setRolloverIcon(UI.ICON_CANCEL_BUTTON);
        cancelButton.setPressedIcon(UI.ICON_CANCEL_BUTTON_ENABLE);
        cancelButton.setDisabledIcon(UI.ICON_CANCEL_BUTTON_DISABLE);
    }

    /**
     * 设置JTextArea样式
     */
    private void setTextArea() {
        textArea1.setEnabled(false);
        textArea1.setLineWrap(true);//激活换行
        textArea1.setWrapStyleWord(true);// 激活换行不断字,及一个单词不会分为两行显示
        /**
         * 滚动条绑定JTextArea
         */
        scroll.setViewportView(textArea1);
    }

    /**
     * 设置JProgressBar
     */
    private void setProgressBar() {
        progressBar1.setBorderPainted(false);
        progressBar1.setValue(0);
    }

    /**
     * MyThread类，继承Thread类，实现传输的暂停、取消
     */
    class MyThread extends Thread {
        /**
         * 实现run方法
         */
        @Override
        public void run() {
            startTransfer();
            /**
             * 若是移动文件，则删除源文件
             */
            if (isCancel) return;
            try {
                deleteFiles(sourcePath);
                sourcePath = "";
                textArea1.setText(textArea1.getText() + "源文件已删除  \n");
                scrollToBottom();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        /**
         * 开始传输
         */
        private void startTransfer() {
            isCopy(sourcePath, destinationPath);
        }


        /**
         * @param inputPath  源路径
         * @param outputPath 目标路径
         */
        private void isCopy(String inputPath, String outputPath) {
            File input = new File(inputPath);
            File output = new File(outputPath);
            if (input.isDirectory()) {
                output = new File(output.getAbsolutePath() + File.separator + input.getName());
                output.mkdirs();
                String[] fileArray = input.list();
                for (String i : fileArray) {
                    isCopy(input.getAbsolutePath() + File.separator + i, output.getAbsolutePath());
                }
            } else {
                File temp = new File(output.getAbsolutePath() + File.separator + input.getName());
                try {
                    temp.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    doCopy(input, temp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 执行复制
         *
         * @param source      源路径
         * @param destination 目标路径
         */
        private void doCopy(File source, File destination) throws IOException {
            if (isCancel) return;
            /**
             * 滚动条始终在最下
             */
            textArea1.setText(textArea1.getText() + "正在传输：" + source + "......0%  \n");
            scrollToBottom();

            RandomAccessFile sourceFile = new RandomAccessFile(source, "r");
            FileChannel sourceChannel = sourceFile.getChannel();
            RandomAccessFile destinationFile = new RandomAccessFile(destination, "rw");
            FileChannel destinationChannel = destinationFile.getChannel();
            long leftSize = sourceChannel.size();
            long whole = leftSize;
            long position = 0;
            long writeSize = 2 * 1024 * 1024;//一次传输2M
            int number = 0;
            while (leftSize > 0) {
                if (isCancel) {
                    sourceChannel.close();
                    sourceFile.close();
                    destinationChannel.close();
                    destinationFile.close();
                    return;
                }
                if (isPause) {//暂停时的操作
                    try {
                        thread.sleep(10);
                    } catch (Exception ie) {
                        ie.printStackTrace();
                    }
                    continue;
                }
                if (leftSize < writeSize) writeSize = leftSize;
                /**
                 * 每次传输writeSize字节，transferTo函数返回地是本次传输的字结数
                 */
                writeSize = sourceChannel.transferTo(position, writeSize, destinationChannel);
                position += writeSize;
                leftSize -= writeSize;
                destinationSize += writeSize;
                number = (int) ((double) destinationSize / (double) sourceSize * 100);
                progressBar1.setValue(number);
                progressBar1.setString(String.valueOf(number) + "%");
                number = (int) ((double) position / (double) whole * 100);
                textArea1.setText(textArea1.getText() + "正在传输：" + source + "......" + String.valueOf(number) + "%\n");
                scrollToBottom();
            }
            sourceChannel.close();
            sourceFile.close();
            destinationChannel.close();
            destinationFile.close();
//            deleteFiles(source.getAbsolutePath());
        }
    }

    /**
     * @param folder 文件
     * @return 文件大小
     */
    private long getSize(File folder) {
        long length = 0;
        if (folder.isFile()) {
            return folder.length();
        }
        File[] files = folder.listFiles();
        int count = files.length;
        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                length += files[i].length();
            } else {
                length += getSize(files[i]);
            }
        }
        return length;
    }

    /**
     * @param path 所要删除的路径
     */
    private void deleteFiles(String path) throws IOException {
        Path directory = Paths.get(path);
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });

    }

    /**
     * 用以控制scroll到最底部
     */
    private void scrollToBottom() {
        p.setLocation(0, textArea1.getLineCount() * 20);
        scroll.getViewport().setViewPosition(p);
    }

}

