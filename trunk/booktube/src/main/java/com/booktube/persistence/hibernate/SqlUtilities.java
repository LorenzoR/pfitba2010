package com.booktube.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.booktube.model.User.Gender;
import com.booktube.pages.AgeFilterOption;
import com.booktube.pages.DropDownElementPanel;
import com.booktube.pages.FilterOption;
import com.booktube.pages.MiscFilterOption;
import com.booktube.pages.OriginFilterOption;

public class SqlUtilities {
	public static String generateWhereClause(OriginFilterOption origin, AgeFilterOption age, MiscFilterOption misc ){		
		ArrayList<String> restrictions = new ArrayList<String>();
		
		if( misc != null ){
			List<DropDownElementPanel> miscOptions = misc.getElements();
			for (DropDownElementPanel element : miscOptions) {
				restrictions.add( generateRestriction(element.getTableFieldName(), "=", element.getSelectedValue()) );
			}
		}
		
		if( origin != null ){
			restrictions.add(generateRestriction("city", "=", origin.getSelectedCity()) );
			restrictions.add(generateRestriction("country", "=",origin.getSelectedCountry()));
		}
		
		if( age != null ){
			restrictions.add(generateRestriction("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0", ">=", age.getSelectedMinAge()));
			restrictions.add(generateRestriction("DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( BIRTHDATE ) ) ,  '%Y' ) +0", "<=", age.getSelectedMaxAge()));
		}
		return concatRestrictions(restrictions);
	}
	
	// Devuelve toda la clausula where con las restricciones
	// Si no hay restricciones devuelve una cadena vacia
	private static String concatRestrictions(ArrayList<String>restrictions) {
		StringBuffer buff = new StringBuffer();
		String result = "";		
		
		boolean clauseIsNotBlank = false;
		for (String aRestriction : restrictions) {
			if( StringUtils.isNotBlank(aRestriction)){
				if( clauseIsNotBlank )
					buff.append(" and ");
				buff.append(aRestriction);
				clauseIsNotBlank = true;
			}
		}			
		
		if( StringUtils.isNotBlank(buff.toString()) )
			result = "where "+buff.toString();
		return result;	
	}

	// Genera cadena con una restriccion para la clausura where, o una
	// cadena vacia si la restriccion no debe estar en el query.
	// Se  usa para los combo box. Si no se selecciona ninguna opcion,
	// queda la cadena "Seleccione ..", y la restriccion no debe agregarse
	// En caso se requiera alguna transformacion del valor obtenido del combo
	// la funcion getFieldValue() se encarga
	private static String generateRestriction(String fieldName, String comparator,  String value) {		
		StringBuffer restriction = new StringBuffer();
		if( value != FilterOption.listFirstOption )
			restriction.append(fieldName+comparator+getFieldValue(value));
		return restriction.toString();
	}

	// Se fija si debe transformar el valor obtenido de un combo a un valor
	// equivalente que se pueda usar en la clausula where
	private static String getFieldValue(String value) {
		String comillas = "\"";
		String resp = value;
		if( value == "Masculino")
			resp =  String.valueOf(Gender.MALE.ordinal());
		else if( value == "Femenino" )
			resp = String.valueOf(Gender.FEMALE.ordinal());
		return comillas+resp+comillas;
	}

}
