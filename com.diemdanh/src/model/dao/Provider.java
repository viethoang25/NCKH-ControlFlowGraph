package model.dao;

import java.io.*;
import java.util.*;

import manager.Constants;
import model.bean.*;

public class Provider {

	private static Provider instance = new Provider();

	private Provider() {

	}

	public static Provider getInstance() {
		return instance;
	}

	public List<Student> getAllStudents() {
		List<Student> list = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new FileReader(Constants.FILE_STUDENTS_LIST));
			String line = br.readLine();
			List<String> listId = new ArrayList<String>();
			while ((line = br.readLine()) != null && line.length() > 0) {
				String[] item = line.split("\\|");
				if (!listId.contains(item[0])) {
					listId.add(item[0]);
					list.add(new Student(item[0], item[1]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return list;
	}

	public List<Unit> getAllUnits() {
		List<Unit> list = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new FileReader(Constants.FILE_STUDENTS_LIST));
			String line = br.readLine();
			List<String> listId = new ArrayList<String>();
			while ((line = br.readLine()) != null && line.length() > 0) {
				String[] item = line.split("\\|");
				if (!listId.contains(item[2])) {
					listId.add(item[2]);
					list.add(new Unit(item[2], item[3]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return list;
	}
	
	public Account checkAccount(String username, String password) {
		Account account = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new FileReader(Constants.FILE_ACCOUNT));
			String line = br.readLine();
			List<String> listId = new ArrayList<String>();
			while ((line = br.readLine()) != null && line.length() > 0) {
				String[] item = line.split("\\|");
				if (item[0].equals(username) && item[1].equals(password)) {
					account = new Account(item[0], item[1], item[2], item[3]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return account;
	}
	
	public List<Unit> getUnitsOfStudent(String studentId) {
		List<Unit> list = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new FileReader(Constants.FILE_STUDENTS_LIST));
			String line = br.readLine();
			while ((line = br.readLine()) != null && line.length() > 0) {
				String[] item = line.split("\\|");
				if (item[0].equals(studentId)) {
					list.add(new Unit(item[2], item[3]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return list;
	}
}
