//Menu 类
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;



public class Menu {
    private Scanner scan = new Scanner(System.in);

    //输出菜单
    public void showMenu() {
        System.out.println("-------MENU-----");
        System.out.println("1. 分析目录中的源程序文件");
        System.out.println("2. 查看分析结果");
        System.out.println("0. 退出程序");
        System.out.println("----------------");
        System.out.print("请选择：");
        chooseFunction();
    }

    //选择执行的操作
    private void chooseFunction() {
        int type = scan.nextInt();
        if (type == 1)
            analyseSrcFiles();//调用下文私有方法
        else if (type == 2)
            checkResult();//调用下文私有方法
        if(type == 1 || type == 2)
            showMenu();
    }

    //操作1
    private void analyseSrcFiles() {
        System.out.println();
        System.out.print("输入目录名称：");
        String dirName = scan.next();

        //指定分析目录
        File dir = new File(dirName);
        Directory directory = new Directory(dir);
        if(!dir.isDirectory()){
            System.out.println("错误：["+dirName+"]不是目录名或不存在！\n");
        }else{
            //调用directory 类 analyseSubDir 方法
            directory.analyseSubDir();
            System.out.println("分析完成\n");
        }
    }

    private void checkResult() {
        System.out.println();
        File file = new File("Result");
        File[] files = file.listFiles();
        if(files == null){
            System.out.println("还没有分析结果！");
        }else{
            System.out.println("----------------");
            for(int i=0; i<files.length; ++i){
                System.out.println(i+1 + "." + files[i].getName());
            }
            System.out.println("----------------");
            System.out.print("选择要查看的结果文件(0表示放弃)：");
            int type = scan.nextInt();
            if(type == 0)
                return;

            //下标 = 序号 - 1
            checkFile(files[type-1]);
            System.out.println();
        }
    }

    //传入形参：操作的文件，相当于水池
    private void checkFile(File file){
        BufferedReader br = null;
        try {
            //指定输入流，相当于水管
            br = new BufferedReader(new FileReader(file));
            while(true){
                String data = br.readLine();
                if(data == null)
                    break;
                System.out.println(data);
            }
        } catch (IOException e) {
            //eclipse自动生成的异常处理
            e.printStackTrace();
        } finally{
            //关闭输入流，避免发生其他错误
            if(br != null){//确保下文close不会访问空指针
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}