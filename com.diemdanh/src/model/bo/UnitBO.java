package model.bo;

import java.util.List;

import model.bean.Unit;
import model.dao.Provider;

public class UnitBO {

	public static List<Unit> getUnitsOfStudent(String studentId) {
		return Provider.getInstance().getUnitsOfStudent(studentId);
	}
}
