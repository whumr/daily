package com.blossom.test.util;

public class GroupError {

	public enum Condition {

        alread_in_group("alread_in_group", 100, "you are already in the group."),
        alread_applied("alread_applied", 101, "you have applied to join the group."),
        group_not_exsist("group_not_exsist", 102, "group not exsist."),

        apply_already_processed("apply_already_processed", 103, "apply has already been processed."),

        member_not_exsist("member_not_exsist", 104, "member not exsist."),
        
        bad_request("bad_request", 401, "bad request."),
        no_permission("no_permission", 402, "you have no permission to do that."),
        
        server_error("server_error", 500, "server error.");
        
        public static Condition fromXMPP(String condition) {
            if (condition == null) {
                throw new NullPointerException();
            }
            condition = condition.toLowerCase();
            if (alread_in_group.getValue().equals(condition)) {
                return alread_in_group;
            } else {
                throw new IllegalArgumentException("Condition invalid:" + condition);
            }
        }
        
        public static Condition fromLegacyCode(int code) {
            if (alread_in_group.getCode() == code) {
                return alread_in_group;
            } else {
                throw new IllegalArgumentException("Code invalid:" + code);
            }
        }
        
        private String value;
        private String msg;
        private int code;

        private Condition(String value, int code, String msg) {
            this.value = value;
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

		public String getMsg() {
			return msg;
		}
		
		public void appendMsg(String msg) {
			this.msg = this.msg + msg;
		}
	}
}
