package utils;

import android.os.Environment;

import org.kymjs.aframe.core.KJException;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * User: niuwei(nniuwei@163.com)
 * Date: 2014-12-21
 * Time: 00:07
 * FIXME
 */
public class FileUtils {
    private static FileUtils instance = null;

    /**
     * 单例模式
     * */
    public static FileUtils getInstance(){
        if (instance == null) {
            synchronized (FileUtils.class) {
                if (instance == null) {
                    instance = new FileUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 判断SD卡是否存在
     * */
    private boolean isSDCardExist(){
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 获取根文件夹的文件路径
     * @return String
     * */
    private String getRootDir(){
        String root = null;
        if (isSDCardExist()) {
            root = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return root;
    }

    /**
     * 合并两个路径
     * @param path1
     * 				第一个路径-String
     * @param path2
     * 				第二个路径-String
     * @return path1 + "/" + path2
     * */
    private String combinePath(String path1,String path2){
        return path1 + "/" + path2;
    }


    /**
     * 创建文件夹,如果文件夹存在则不创建,否则创建
     * @param folderName
     * 					文件夹名称-String
     * @return
     * 					文件夹的绝对路径
     * */
    public String createFolder(String folderName){
        String root = getRootDir();
        File file = new File(combinePath(root, folderName));
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    /**
     * 在一个特定的文件夹当中创建文件
     * @param fileName
     * 			文件名-String
     * @param folderName
     * 			文件夹名称-String
     * @return
     * 			文件的绝对路径-String
     * @throws IOException
     * */
    public String createFile(String fileName,String folderName) throws IOException {
        String dir = createFolder(folderName);
        File file = new File(combinePath(dir, fileName));
        if (!file.exists()) {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    /**
     * @param fileData byte数组
     * @param fileName 文件名称
     * @param folderPath 文件夹路径
     * */
    public void saveFileCache(byte[] fileData,String folderPath, String fileName){
        File folder = new File(folderPath);
        folder.mkdirs();
        File file = new File(folderPath,fileName);
        ByteArrayInputStream is = new ByteArrayInputStream(fileData);
        OutputStream os = null;
        if (!file.exists()){
            try {
                file.createNewFile();
                os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while (-1 != (len = is.read(buffer))) {
                    os.write(buffer, 0, len);
                }
                os.flush();
            } catch (Exception e) {
                throw new KJException(FileUtils.class.getClass()
                        .getName(), e);
            } finally {
                closeIO(is, os);
            }
        }
    }

    /**
     * 关闭流
     *
     * @param closeables
     */
    public void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                throw new KJException(FileUtils.class.getClass()
                        .getName(), e);
            }
        }
    }
}
