//SourceFile 类
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SourceFile {
    private File file;
    private long numOfLines;//单个源码文件行数
    private long numOfBlank;//单个源码文件空行数
    private long bytes;//单个源码文件字节数

    //参数为源码文件
    public SourceFile(File file){
        this.file = file;
        bytes = file.length();
    }

    private void getNumOfLinesAndBlank(){
        //文件操作原理同 Menu 类
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            //从源码文件中读取每一行
            while(true){
                String line = br.readLine();
                if (line == null) //文件读取结束
                    break;
                numOfLines++; //每读一行，总行数加一
                if("".equals(line)){ //读到空行
                    numOfBlank++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public long getNumOfLines(){
        if(numOfLines == 0) //第一次调用计算后返回，下次无需计算直接返回
            getNumOfLinesAndBlank();
        return numOfLines;
    }
    public long getNumOfBlank(){
        if(numOfBlank == 0)//第一次调用计算后返回，下次无需计算直接返回
            getNumOfLinesAndBlank();
        return numOfBlank;
    }
    public long getBytes(){
        return bytes;
    }
}