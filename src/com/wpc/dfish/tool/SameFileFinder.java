package com.wpc.dfish.tool;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SameFileFinder {
	public SameFileFinder(){}
	private String targetFolder;
	private FileFilter fnFilter=null;
	private ArrayList<FileSample> list;
	private ArrayList<ArrayList<FileSample>> sameSizeGroups;
	private List<List<File>> sameGroups;
	public String getTargetFolder() {
		return targetFolder;
	}
	public void setTargetFolder(String targetFolder) {
		this.targetFolder = targetFolder;
	}
	public static void main(String[] args){
		SameFileFinder tool=new SameFileFinder();
		tool.setTargetFolder("E:/Tencent/QQ2009/27542726/Image");
		tool.goThrough();
		for(ArrayList<FileSample> group:tool.sameSizeGroups){
			System.out.println("发现一组疑似数据");
			for(FileSample fs:group){
				//这组疑似数据中的文件长度都一样。
				//需要分析其内容，才能真正确定文件的是否一致。
				System.out.println("\t"+fs.size+"\t"+fs.file.getAbsolutePath());
			}
		}
	}
	public void goThrough(FileFilter filenameFilter) {
		fnFilter=filenameFilter;
		list=new ArrayList<FileSample>();
		sameSizeGroups=new ArrayList<ArrayList<FileSample>>();
		System.out.println("从文件系统收集文件信息");
		goThrough(new File(targetFolder));
		System.out.println("文件数据收集完毕");
		checkSameSize();//先判定那些大小一样的文件
		sameGroups=new ArrayList<List<File>>();
		for(ArrayList<FileSample> group:sameSizeGroups){
			List<File> files=new ArrayList<File>();
			for(FileSample fs:group){
				List<List<File>> sameFileGroup=checkSameFile(files);
			}
		}
	}
	
	/**
	 * 返回若干组 内容一样的文件列表
	 * @param files
	 * @return
	 */
	private List<List<File>> checkSameFile(List<File> files) {
		FileContentGetter fcg=new FileContentGetter();
		List<List<File>> result =new ArrayList<List<File>>();
		List<File> onlyGroup=new ArrayList<File>(files);
		result.add(onlyGroup);
		while(true){
			List<List<File>> newResult =new ArrayList<List<File>>();
			for(List<File> sameGroupFile:result){
				List<LabeledList> gourps=new ArrayList<LabeledList>();
				for(File f:sameGroupFile){
					byte[] label =fcg.getNextBlock(f);
					boolean found=false;
					for(LabeledList ll:gourps){
						if(ll.isSameLabel(label)){
							found=true;
							ll.list.add(f);
							break;
						}
					}
					if(!found){
//						LabeledList grou
//						gourps.add(o);
					}
				}
			}
			
			if(newResult.size()==0)break;
			result=newResult;
		}
		
		fcg.close();
		return result;
	}
	static class LabeledList{
		byte[] label;
		ArrayList<File> list;
		LabeledList(byte[] label){
			this.label=label;
			list=new ArrayList<File>();
		}
		boolean isSameLabel(byte[] another){
			if(another==null||another.length!=label.length)return false;
			for(int i=0;i<another.length;i++){
				if(label[i]!=another[i])return false;
			}
			return true;
		}
	}
	private void register(File sub) {
		FileSample f=new FileSample();
		f.file=sub;
		f.size=sub.length();
		list.add(f);
	}
	private void checkSameSize() {
		// 先按大小排序
		Collections.sort(list,new java.util.Comparator<FileSample>(){
			public int compare(FileSample fs1, FileSample fs2) {
				return (int)(fs1.size-fs2.size);
			}
		});
		long lastFileLength=-1;
		//0字节长度的文件我们认为他是相等的。
		ArrayList<FileSample> group=new ArrayList<FileSample>();
		for(FileSample fs:list){
			if(fs.size>lastFileLength){
				if(group.size()>1){
					//登记Group
					sameSizeGroups.add(group);
				}
				group=new ArrayList<FileSample>();
				lastFileLength=fs.size;
			}
			group.add(fs);
		}
		if(group.size()>1){
			//登记Group
			sameSizeGroups.add(group);
		}
	}
	private void goThrough(File f){
		File[] subs=f.listFiles(fnFilter);
		for(File sub:subs){
			if(sub.isFile()){
				register(sub);
			}else if(sub.isDirectory()){
				goThrough(sub);
			}
		}
	}
	public void goThrough(){
		goThrough(new FileFilter(){
			
			public boolean accept(File file) {
				return true;
			}
		});
	}
	static class FileSample{
		private File file;
		private long size;
	}

	static class FileContentGetter{
		public FileContentGetter(){
			this(8192);
		};
		private int buffSize=0;
		public FileContentGetter(int buffSize){
			this.buffSize=buffSize;
		}
		public byte[] getNextBlock(File file){
			return new byte[buffSize];
		}
		/**
		 * 关闭所有打开的文件
		 */
		public void close() {
			
		}
		/**
		 * 关闭所有打开的文件
		 */
		public void close(File file) {
			
		}
		
	}
}
