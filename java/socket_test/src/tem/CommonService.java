package tem;

import org.json.JSONException;

import tem.Constants.OP_CODE;
import tem.domain.Protocol;
import tem.domain.User;
import tem.util.Util;

public class CommonService {

	private CommonDao commonDao;

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}
	
	public boolean parseCommand(Protocol protocol) {
		String code = Long.toHexString(protocol.getCode());
		switch(code.length()) {
			case 1:
				return handleUser(protocol, code);
		}
		return false;
	}
	
	private boolean handleUser(Protocol protocol, String code) {
		switch (Integer.parseInt(code)) {
			case (int)OP_CODE.REGIST:
				return saveUser(protocol.getContent());
		}
		return false;
	}
	
	public boolean saveUser(String userInfo) {
		try {
			User user = Util.parseUser(userInfo);
			commonDao.saveUser(user);
			return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
