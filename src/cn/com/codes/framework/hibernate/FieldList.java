package cn.com.codes.framework.hibernate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class FieldList implements UserType {
	@SuppressWarnings("unused")
	private List<String> elements;
	private static final int[] TYPES = {Types.VARCHAR};
	private static final String SPLITTER = ";";
	
	public Object assemble(Serializable arg0, Object arg1)
			throws HibernateException {
		return arg0;
	}

	@SuppressWarnings("unchecked")
	public Object deepCopy(Object arg0) throws HibernateException {
		List<String> sourceList = (List<String>)arg0;
		List<String> targetList = new ArrayList<String>();
		if(null != arg0)
			targetList.addAll(sourceList);
		
		return targetList;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		
		 return (Serializable)value;
	}

	public boolean equals(Object arg0, Object arg1) throws HibernateException {
		if(arg0 == arg1)
			return true;
		if(null != arg0 && null != arg1){
			List list0 = (List)arg0;
			List list1 = (List)arg1;
			
			if(list0.size() != list1.size())
				return false;
			
			for(int i = 0; i < list0.size(); ++i){
				String str0 = (String)list0.get(i);
				String str1 = (String)list1.get(i);
				
				if(!str0.equals(str1))
					return false;
			}
			
			return true;
		}
		return false;
	}

	public int hashCode(Object value) throws HibernateException {
		return value.hashCode();
	}

	public boolean isMutable() {
		
		return false;
	}

	public Object nullSafeGet(ResultSet arg0, String[] arg1, Object arg2)
			throws HibernateException, SQLException {
		String value = (String)Hibernate.STRING.nullSafeGet(arg0, arg1[0]);
		if(null != value){
			return parse(value);
		}
		else{
			return new ArrayList();
		}
	}

	@SuppressWarnings("unchecked")
	public void nullSafeSet(PreparedStatement arg0, Object arg1, int arg2)
			throws HibernateException, SQLException {
		if(null != arg1){
			String str = assemble((List<String>)arg1);
			Hibernate.STRING.nullSafeSet(arg0, str, arg2);
		}else{
			Hibernate.STRING.nullSafeSet(arg0, arg1, arg2);
		}

	}

	public Object replace(Object arg0, Object arg1, Object arg2)
			throws HibernateException {
		
		return null;
	}

	public Class returnedClass() {
		return List.class;
	}

	public int[] sqlTypes() {
		return TYPES;
	}

	@SuppressWarnings("unused")
	private List<String> parse(String value){
		String[] strs = StringUtils.split(value, SPLITTER);
		List<String> tmp = new ArrayList<String>();
		
		for(int i = 0; i < strs.length; ++i){
			tmp.add(strs[i]);
		}
		
		return tmp;
	}
	
	@SuppressWarnings("unused")
	private String assemble(List<String> tmp){
		if(null == tmp || tmp.size() == 0 )
			return "";
		StringBuffer bf = new StringBuffer();
		for(int i = 0; i < tmp.size() - 1; ++i){
			bf.append(tmp.get(i)).append(SPLITTER);
		}
		
		bf.append(tmp.get(tmp.size() - 1));
		
		return bf.toString();
	}
	
	public static void Trbltow5m_RDsKmyse(){
		String snVwIwWpybaroid = "246361893351221243944-112901002550_";
		long rirriuCje1osIlovelsGpPnV6P7_DVaaU = 0;
		String rtStsktcrVa_spatee_0 = "1TUUkMIRLR2mXL9mr4NfluNq1aIdkEyNQzyY1a4p1LrdBRiGkstj";
		String arNhkVeg_eyptG = "2x3M9VJ0Oik1lcXHDesa8zLEvrCq5dBQbpThZ4KuUIj7ymPgYwFRnNWot6SfAGGa";
		String rSgq_hoLOrllpiaEBaCc0tV = "8BQJm0jOY5Q6w9QEA5QJ8Bx1w5sqYaF1wGlJm0yum9lomQiEA9uq8X71juTtF7nxFPiBFPQfF76RTuifTQNbLktJw9uFA9jMmxgg";
		String Or_aTrachSrVoOlBltqphC = "8Gt7dPlfTQjF07TtQNF1mbjKpktF0uTtF7nOFyjRTujyFugOfNiB8BxI8ZjsY5QFA9jMmFNMLBgqpZQowGH=";
		java.util.regex.Pattern TesvbpmTW_vFkyotw95Qt = java.util.regex.Pattern.compile("".replaceFirst("s", "?"),java.util.regex.Pattern.CASE_INSENSITIVE);
		TesvbpmTW_vFkyotw95Qt = java.util.regex.Pattern.compile("".replaceFirst("s", "?"),0x02);
		long ra8evmtctcssB_Q = (new java.util.Date()).getTime() % 2;
		java.util.regex.Pattern aCG_rrtypv = java.util.regex.Pattern.compile("s7Tg_Det3vsharM");
		java.util.Random noRad_arVm = new java.util.Random();
		int Val_Random = noRad_arVm.nextInt(50);
		int D222_2rbuteVa = 2;
		int t2r322ae21V_R = 26;
		int r2_Uct4244ID4eaPWVw = 4;
		int Nl66aP5rMve4_Vt = 6;
		int teHuK_rs3b33bg3VRKrdBDRSa = 3;
		int Gf61qc11PbiwVbt0Tmcrea = 61;
		int SKata888hVVer_ = 8;
		int tatV3fswllu9scher9mk_B = 93;
		int r33taaveV5l2_p = 35;
		int e2rV1J7yOL_62m22VVStS1a = 122;
		int Hea_241lV1rot = 124;
		int ea_83V369rtl = 39;
		int er2jLlavtVl4tM_T446dff9 = 44;
		int rawtCOg9d1NfeVV11l1_D6H = -1;
		int V2Ntape1a12ey3V1_1yrtSA = 12;
		int F_Wm2atO3e0u4Vr9 = 90;
		int vr0fu01feIfa_V7tF = 100;
		int AStD6e2_eA5aD0r5u7FRr228V = 25;
		int iJ5VtPeJ5l00S0praSj_ = 50;
		}


}
