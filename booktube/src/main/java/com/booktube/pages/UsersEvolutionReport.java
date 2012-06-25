package com.booktube.pages;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.booktube.service.UserService;


public class UsersEvolutionReport extends ReportPage {
	private static final long serialVersionUID = 6051762145219128009L;
	
	protected AgeFilterOption ageFilter;
	protected OriginFilterOption originFilter;
	protected MiscFilterOption customizedMisc;
	DropDownElementPanel genderDropDownElement;
	@SpringBean
	UserService userService;
	private List<String> allGendersList = userService.getAllGenders();
		
	public UsersEvolutionReport(){
		super();
		// No es necesario WebMarkupContainer pues esta subclase no agrega codigo estatico HTML a la superclase(ReportPage)

		//Agrego las opciones de filtrado segun que reporte se quiere generar
		originFilter = new OriginFilterOption("component");
		reportFilter.addFilterOption(originFilter);
		
		ageFilter = new AgeFilterOption("component");
		reportFilter.addFilterOption(ageFilter);
	
		customizedMisc = new MiscFilterOption("component");
		genderDropDownElement = new DropDownElementPanel("element", "Sex", allGendersList); 
		customizedMisc.addElement(genderDropDownElement);
		reportFilter.addFilterOption(customizedMisc);
		
		String newTitle = "Booktube - Users Evolution Report"; 
		super.get("pageTitle").setDefaultModelObject(newTitle);	
		
		// En esta clase se agrega el boton submit y el evento onSubmit pues cada Reporte
		// necesitara informacion diferente y ejecutara graficos diferentes
		form.add(new Button("renderReport", new Model<String>("Graficar")) {
			private static final long serialVersionUID = 6743737357599494567L;

			@Override
			public void onSubmit() {
				System.out.println("min="+ageFilter.getSelectedMinAge()+" Max="+ageFilter.getSelectedMaxAge());
				System.out.println("pais="+originFilter.getSelectedCountry()+" ciudad="+originFilter.getSelectedCity());
				System.out.println("sexo="+genderDropDownElement.getSelectedGender());
			}
		});


	}
	
}
