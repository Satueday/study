//Directory 类
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;

public class Directory {
    private File directory;
    private long numOfSrcFile;//该目录下所有文件数
    private long sumLines;//所有源码文件总行数
    private long sumBlank;//所有源码文件总空行数
    private long sumBytes;//所有源码文件总字节数

    //形参为菜单中输入的目录，非法目录已被 Menu 处理，该 File 必为合法
    public Directory(File dir) {
        directory = dir;
    }


    private void changeOut(){
        File content = new File("Result");//相对当前Project的路径，与src同级
        content.mkdir();//生成目录文件夹
        File resultTxt = new File("Result\\"+directory.getName() + ".txt");
        try {
            resultTxt.createNewFile();//生成保存 result 的文件
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            //从控制台重定向输出到文件中
            PrintStream ps = new PrintStream(resultTxt);
            System.setOut(ps);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void analyseSubDir() {
        //提前保存控制台，输出到文件完毕后恢复输出到控制台
        PrintStream out = System.out;
        changeOut();
        System.out.println("[" + directory.getAbsolutePath() + "] Result:\n\nFiles detail:");
        dfsAnalyse(directory, 0);//递归统计目录下每个源码文件数据
        System.out.println("Total:");
        System.out.printf("\t%5d Java Files\n\t%5d lines in files\n\t%5d blank lines\n\t%5d bytes", numOfSrcFile,
                sumLines, sumBlank, sumBytes);
        System.setOut(out);//恢复输出到控制台
    }

    //判断文件是否为源码文件
    private boolean isJava(File file) {
        String name = file.getName();
        int len = name.length();
        String postName = (String) name.subSequence(len - 5, len);//参数区间左闭右开，获取文件名后五个字母形成的子串
        // System.out.println(postName);
        return ".java".equals(postName);
    }

    private void sortFiles(File[] files) {
        //匿名内部类构造比较器，对目录下文件排序
        Arrays.sort(files, new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                if (o1.isFile() && o2.isFile()) {//同为文件，按字典序排序
                    return o1.getName().compareTo(o2.getName());
                } else if (o1.isDirectory() && o2.isDirectory()) {//同为目录，按字典序排序
                    return o1.getName().compareTo(o2.getName());
                } else if (o1.isDirectory() && o2.isFile()) {//目录“小于”文件，排在前面
                    return -1;
                } else if (o1.isFile() && o2.isDirectory()) {
                    return 1;
                } else {
                    return 0;//理论上不会走到这里，为了确保返回值为 int，编译不报错
                }
            }

        });
    }

    //File形参为当前递归的文件，level为递归的层数，也是缩进的倍数
    private void dfsAnalyse(File file, int level) {
        for (int i = 0; i < level; ++i)
            System.out.print("    ");
        if (file.isDirectory()) {//递归到目录继续递归
            System.out.println("+" + file.getName());
            File[] files = file.listFiles();
            sortFiles(files);
            for (int i = 0; i < files.length; ++i) {
                dfsAnalyse(files[i], level + 1);
            }
        } else if (isJava(file)) {//递归文件统计信息并输出
            // System.out.println("isJava: " + isJava(file));
            System.out.print("-" + file.getName());
            SourceFile srcFile = new SourceFile(file);
            numOfSrcFile++;
            sumBlank += srcFile.getNumOfBlank();
            sumLines += srcFile.getNumOfLines();
            sumBytes += srcFile.getBytes();
            int spaceLength = file.getName().length();
            for (int i = spaceLength; i < 35; ++i) {//对齐，为了方便没有统计最长的文件名，随便写了个35
                System.out.print(" ");
            }
            System.out.println("\tTotal:\t" + String.format("%5d", srcFile.getNumOfLines()) + ", Blank:\t"
                    + String.format("%5d", srcFile.getNumOfBlank()) + ",\t" + String.format("%5d", srcFile.getBytes())
                    + " Bytes");
        }
    }
}