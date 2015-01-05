package tem;

import java.sql.Types;

import org.springframework.jdbc.core.JdbcTemplate;

import tem.domain.User;

public class CommonDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void saveUser(User user) {
		String sql = "insert into users(name, pass, age, gender, nickname) values (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, 
			new Object[] {
				user.getName(),
				user.getPass(),
				user.getAge(),
				user.getGender(),
				user.getNickname()}, 
			new int[]{
				Types.VARCHAR,
				Types.VARCHAR,
				Types.INTEGER,
				Types.INTEGER,
				Types.VARCHAR}
		);
		System.out.println("-----------");
	}
}
