package lxdemo.springcloud.systemAdmin.utils;

import java.util.HashMap;
import java.util.Map;

public class ResMsgUtil {
	
	public static final String MSG = "msg";
	public static final String CODE = "code";
	public static final String DATA = "data";
	public static final String CODE_SUCESS = "200";
	public static final String CODE_FAIL = "300";
	public static final String CODE_ERROR = "500";

	public static Map<String, Object> getFailResp(String msg){
		Map<String, Object> res = new HashMap<String, Object>();
		res.put(MSG, msg);
		res.put(CODE, CODE_FAIL);
		return res;
	}
	
	public static Map<String, Object> getSuccessResp(Map<String, Object> data){
		Map<String, Object> res = new HashMap<String, Object>();
		res.put(CODE, CODE_SUCESS);
		res.put(DATA, data);
		return res;
	}
	
	public static Map<String, Object> getSuccessResp(String data){
		Map<String, Object> res = new HashMap<String, Object>();
		res.put(CODE, CODE_SUCESS);
		res.put(DATA, data);
		return res;
	}
}
