package cn.com.codes.common.util;

  

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import cn.com.codes.common.util.CpuTest;
  

  
/** 
 * Cpu数据 
 *  
 * 使用Sigar获得CPU的基本信息、使用百分比、使用时间 
 *  
 */  
public class CpuTest {  
    private CpuInfo info;  
    private CpuPerc perc;  
    private Cpu timer;  
    public CpuTest() {  
    }  
  
    public void populate(Sigar sigar) throws SigarException {  
        info = sigar.getCpuInfoList()[0];  
        perc = sigar.getCpuPerc();  
        timer = sigar.getCpu(); 
    }  
  
    public static CpuTest gather(Sigar sigar) throws SigarException {  
    	CpuTest data = new CpuTest();  
        data.populate(sigar);  
        return data;  
    }  
  
    public static void main(String[] args) throws Exception {  
        Sigar sigar = new Sigar();  
        CpuTest cpuTest = CpuTest.gather(sigar);  
        System.out.println(cpuTest.getInfo());  
        System.out.println(cpuTest.getPerc());  
    }

	public CpuInfo getInfo() {
		return info;
	}

	public void setInfo(CpuInfo info) {
		this.info = info;
	}

	public CpuPerc getPerc() {
		return perc;
	}

	public void setPerc(CpuPerc perc) {
		this.perc = perc;
	}

	public Cpu getTimer() {
		return timer;
	}

	public void setTimer(Cpu timer) {
		this.timer = timer;
	}  
  
} 