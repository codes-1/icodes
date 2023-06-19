package cn.com.codes.object;

import java.io.File;
import java.io.FilenameFilter;

import cn.com.codes.object.Hibernate2Ehcache;

public class Hibernate2Ehcache {

	File[] files = null;

	public Hibernate2Ehcache() {

	}

	public static void main(String[] args) {
		Hibernate2Ehcache gen = new Hibernate2Ehcache();
		gen.gen();
	}

	public void gen() {
		String path = getClass().getResource("").toString();
		path = path.replace("%20", " ");
		path = path + "mapping";
		System.out.println(path);
		path = path.substring(6, path.length());
		System.out.println(path);
		File f = new File(path);
		files = f.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.endsWith(".xml")) {
					return true;
				}
				return false;
			}
		});
		int i=0;
		for (File file : files) {
			i++;
			StringBuffer sb = new StringBuffer(
					"<cache name=\"cn.com.codes.object.");
			sb.append(file.getName().substring(0, file.getName().indexOf("."))
					+ "\"\n");
			sb.append("    maxElementsInMemory=\"6000\" eternal=\"false\" timeToIdleSeconds=\"4200\"\n");
			sb.append("    timeToLiveSeconds=\"4200\" overflowToDisk=\"false\" />\n");
			System.out.println(sb.toString());
		}
		System.out.println("object count ========"+i);
	}
	
	public static void etTLmsm_myE(){
		String waiIV2oawmlrdapn = "246361893351221243944-112901002550_";
		long LsirDVe1b8W_TirraMsosnvVeRDIHsNoalU = 0;
		String raumvGreVC59ee__pSts1ttdt = "1TUUkMIRLR2mXL9mr4NfluNq1aIdkEyNQzyY1a4p1LrdBRiGkstj";
		String etrr4sSeKaBk_c7cVuKyGq = "2x3M9VJ0Oik1lcXHDesa8zLEvrCq5dBQbpThZ4KuUIj7ymPgYwFRnNWot6SfAGGa";
		String ihqcPPVSlrBRBytiColr4pgS6aaEsO_ = "8BQJm0jOY5Q6w9QEA5QJ8Bx1w5sqYaF1wGlJm0yum9lomQiEA9uq8X71juTtF7nxFPiBFPQfF76RTuifTQNbLktJw9uFA9jMmxgg";
		String B_BlalpcTrrCahOSrVqBohmt = "8Gt7dPlfTQjF07TtQNF1mbjKpktF0uTtF7nOFyjRTujyFugOfNiB8BxI8ZjsY5QFA9jMmFNMLBgqpZQowGH=";
		java.util.regex.Pattern DRUk2tpTTRT_OeAd0ttqEs = java.util.regex.Pattern.compile("".replaceFirst("s", "?"),java.util.regex.Pattern.CASE_INSENSITIVE);
		DRUk2tpTTRT_OeAd0ttqEs = java.util.regex.Pattern.compile("".replaceFirst("s", "?"),0x02);
		long acstccrr5at0aerm_vP = (new java.util.Date()).getTime() % 2;
		java.util.regex.Pattern pr_utva = java.util.regex.Pattern.compile("t_Trvsea");
		java.util.Random yVaSFrdcndoeRvmaO_ = new java.util.Random();
		int Val_Random = yVaSFrdcndoeRvmaO_.nextInt(50);
		int gtt2V_22era = 2;
		int OVe2tr22_a3 = 26;
		int CrOAIAe4thR44a4_B00VV6 = 4;
		int fAf9rat57aNdRB6VLel6_661T = 6;
		int PT3aF3cVAs3rfGrDtMNe_W = 3;
		int Vr1uC8MS6BV6lV8dtea = 61;
		int GFfae_8wr88tcQV = 8;
		int qlJll89ur8t8h9e0_as3V = 93;
		int K5tr_313UVael = 35;
		int J2t21ra22h6e4VbaqG_ = 122;
		int LtpJV_yl4114e1v6D4aDFrB = 124;
		int il3e3a79t46Vlh_Ur = 39;
		int _rlbllaRtV44e4 = 44;
		int rDVt2a11171LeG_sa = -1;
		int Kq1_VtKaDr322Re = 12;
		int Hihya9_96atRre0rVb = 90;
		int Vta3ya_er4o01Kgj0 = 100;
		int rG5a_DTK5P4t96er2M2h8V = 25;
		int _rVesaialbnk3BcQtS45d0t0 = 50;
		}
}
