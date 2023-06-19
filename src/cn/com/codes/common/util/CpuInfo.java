package cn.com.codes.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarLoader;
import org.hyperic.sigar.cmd.Shell;
import org.hyperic.sigar.cmd.SigarCommandBase;

import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.common.util.CpuInfo;


public class CpuInfo extends SigarCommandBase {

	public boolean displayTimes = true;

	public CpuInfo(Shell shell) {
		super(shell);
	}

	public CpuInfo() {
		super();
	}

	public String getUsageShort() {
		return "Display cpu information";
	}

	private void output(CpuPerc cpu) {
		println("User Time....." + CpuPerc.format(cpu.getUser()));
		println("Sys Time......" + CpuPerc.format(cpu.getSys()));
		println("Idle Time....." + CpuPerc.format(cpu.getIdle()));
		println("Wait Time....." + CpuPerc.format(cpu.getWait()));
		println("Nice Time....." + CpuPerc.format(cpu.getNice()));
		println("Combined......" + CpuPerc.format(cpu.getCombined()));
		println("Irq Time......" + CpuPerc.format(cpu.getIrq()));
		if (SigarLoader.IS_LINUX) {
			println("SoftIrq Time.." + CpuPerc.format(cpu.getSoftIrq()));
			println("Stolen Time...." + CpuPerc.format(cpu.getStolen()));
		}
		println("");
	}

	public void output(String[] args) throws SigarException {
		org.hyperic.sigar.CpuInfo[] infos = this.sigar.getCpuInfoList();

		CpuPerc[] cpus = this.sigar.getCpuPercList();

		org.hyperic.sigar.CpuInfo info = infos[0];
		long cacheSize = info.getCacheSize();
		println("Vendor........." + info.getVendor());
		println("Model.........." + info.getModel());
		println("Mhz............" + info.getMhz());
		println("Total CPUs....." + info.getTotalCores());
		if ((info.getTotalCores() != info.getTotalSockets()) || (info.getCoresPerSocket() > info.getTotalCores())) {
			println("Physical CPUs.." + info.getTotalSockets());
			println("Cores per CPU.." + info.getCoresPerSocket());
		}

		if (cacheSize != Sigar.FIELD_NOTIMPL) {
			println("Cache size...." + cacheSize);
		}
		println("");

		if (!this.displayTimes) {
			return;
		}

		for (int i = 0; i < cpus.length; i++) {
			println("CPU " + i + ".........");
			output(cpus[i]);
		}

		println("Totals........");
		output(this.sigar.getCpuPerc());

		StringBuffer sb=new StringBuffer(getCPUSerial());
		
		String[] interfaces = sigar.getNetInterfaceList();
		if(interfaces!=null || interfaces.length>0)
			sb.append(sigar.getNetInterfaceConfig(interfaces[0]).getHwaddr());

		org.hyperic.sigar.FileSystem[] filesystems = sigar.getFileSystemList();
		if(filesystems!=null || filesystems.length>0)
			sb.append(getHDSerial(filesystems[0].getDevName()));
		
		//System.out.println(sb.toString());
	}

	public static void main(String[] args) throws Exception {
		new CpuInfo().processCommand(args);
		
//		CpuInfo info = new CpuInfo() ;
//		org.hyperic.sigar.FileSystem[] filesystems = info.sigar.getFileSystemList();
//		if(filesystems!=null || filesystems.length>0){
//			for(int i=0 ; i<filesystems.length; i++){
//				System.out.println(getHDSerial(filesystems[i].getDevName()));
//			}
//		}
//		System.out.println(info.getCPUSerial());
	}

	/**
	 * Gets the average CPUs idle percentage.
	 * 
	 * @return The idle percentage, a value of 1 stands for a fully loaded CPU
	 */
	public static double getCpuIdle() {
		Sigar sigar = null;
		try {
			sigar = new Sigar();
			return sigar.getCpuPerc().getIdle();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (sigar != null)
				sigar.close();
		}
		return 0;
	}

	public static String getHDSerial(String drive) {
		String result = "";
		try {
			// File file = File.createTempFile("tmp",".vbs");
			//File file = File.createTempFile("tmp", ".vbs");
			PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
			File file = new File(conf.getContextPath().split("WEB-INF")[0]+File.separator+"mypmUserFiles"+File.separator+"null"+File.separator +"hd.vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
					+ "Set colDrives = objFSO.Drives\n" + "Set objDrive = colDrives.item(\"" + drive + "\")\n"
					+ "Wscript.Echo objDrive.SerialNumber";
			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
			file.delete();
		} catch (Exception e) {

		}
		if (result.trim().length() < 1 || result == null) {
			result = "NO_DISK_ID";

		}

		return result.trim();
	}

	public static String getCPUSerial() {
		String result = "";
		try {
			//File file = File.createTempFile("tmp", ".vbs");
			PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
			File file = new File(conf.getContextPath().split("WEB-INF")[0]+"mypmUserFiles"+File.separator+"null"+File.separator +"hd.vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);

			String vbs = "On Error Resume Next \r\n\r\n" + "strComputer = \".\"  \r\n"
					+ "Set objWMIService = GetObject(\"winmgmts:\" _ \r\n"
					+ "    & \"{impersonationLevel=impersonate}!\\\\\" & strComputer & \"\\root\\cimv2\") \r\n"
					+ "Set colItems = objWMIService.ExecQuery(\"Select * from Win32_Processor\")  \r\n "
					+ "For Each objItem in colItems\r\n " + "    Wscript.Echo objItem.ProcessorId  \r\n "
					+ "    exit for  ' do the first cpu only! \r\n" + "Next                    ";

			fw.write(vbs);
			fw.close();
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
			file.delete();
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		if (result.trim().length() < 1 || result == null) {
			result = "NO_CPU_ID";
		}
		return result.trim();
	}
}
