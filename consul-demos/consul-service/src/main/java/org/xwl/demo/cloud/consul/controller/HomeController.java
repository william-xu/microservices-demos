package org.xwl.demo.cloud.consul.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	private String msg = "Hello, ";
	
	@Autowired
	private DataSource ds;
	
	@Value("${server.port}")
	private Integer port;
	
	@RequestMapping("/")
	public String home() {
		String name = null;
		try(Connection conn = ds.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("select user_id, user_name from user where user_id=?");
			ps.setString(1, "xwl");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				name = rs.getString(2);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msg + (name == null ? ", world!" : name + "! (port=" + port + ")");
	}

	
}
