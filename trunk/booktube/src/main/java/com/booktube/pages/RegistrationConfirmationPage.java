package com.booktube.pages;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;


import com.booktube.model.User;
import com.booktube.service.UserService;

public class RegistrationConfirmationPage extends BasePage {
	private static final long serialVersionUID = -4132958291160391804L;
	
	@SpringBean
	UserService userService;
	
	public RegistrationConfirmationPage(PageParameters pageParameters) {

		//Verificamos si tenemos los parametros necesarios para procesar la activacion de cuenta
		if ( pageParameters.get("id").isEmpty() || pageParameters.get("code").isEmpty() ) {
			Logger.getLogger(this.getClass()).info("Los parametros necesarios para confirmar la registracion y activar la cuenta NO estan presentes");
			throw new AbortWithHttpErrorCodeException(404);
		}
		
		final Long id = pageParameters.get("id").toLong();		
		final String secret = pageParameters.get("code").toString();
		User user=userService.getUser(id);
		
		StringBuffer message = new StringBuffer("");
		String errorMessage = "No se pudo activar la cuenta de usuario. Por favor, vuelva a registrarse.";
		String okMessage = "Su cuenta se ha activado.\nAhora puede loguearse y disfrutar de la red social para escritores.";
		String alreadyActiveMessage = "Su cuenta ya esta activa.\nSi no recuerda su contraseña, tiene la opción \"Olvidé mi contraseña\" junto al botón Login";
			
		
		WebMarkupContainer parent = new WebMarkupContainer("registrationDetails");
		parent.setOutputMarkupId(true);
		add(parent);
		parent.add( new Label("messageTitle","Registración Booktube") );		
		
		//Primero verificamos si el ID de usuario existe
		if( user == null ){
			parent.add( new Label("welcomeMessage", "Problemas durante la registración"));
			message.append(errorMessage);			
			Logger.getLogger(this.getClass()).info("No se pudo recuperar informacion del usuario con id "+id);
		}else{
			//Luego verificamos si la cuenta ya esta activa o no
			if( user.getIsActive() ){
				parent.add( new Label("welcomeMessage", "Atención !!!"));
				message.append(alreadyActiveMessage);
				Logger.getLogger(this.getClass()).info("La cuenta ya estaba activa, no hacemos nada para el usuario con id "+id);
			}else{
				//Finalmente intentamos activar la cuenta
				if( userService.activateUserAccount(id, secret) ){
					parent.add( new Label("welcomeMessage", "Bienvenido a Booktube !!!"));
					message.append(okMessage);
					Logger.getLogger(this.getClass()).info("Se activo la cuenta de usuario con id "+id);
				}else{
					parent.add( new Label("welcomeMessage", "Problemas durante la registración"));
					message.append(errorMessage);
					Logger.getLogger(this.getClass()).info("El secreto guardado no coincide con el recibido para el usuario con id "+id);
				}
			}		
		}		
		parent.add( new Label("message",message.toString()));
		
	}
		
	@Override
	protected void setPageTitle() {
		// TODO Auto-generated method stub

	}

}
