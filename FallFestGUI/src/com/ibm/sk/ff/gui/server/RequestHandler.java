package com.ibm.sk.ff.gui.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import com.ibm.sk.ff.gui.Facade;
import com.ibm.sk.ff.gui.common.GUIOperations;
import com.ibm.sk.ff.gui.common.mapper.Mapper;
import com.ibm.sk.ff.gui.common.objects.gui.GAntFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GAntObject;
import com.ibm.sk.ff.gui.common.objects.gui.GFoodObject;
import com.ibm.sk.ff.gui.common.objects.gui.GHillObject;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectTypes;
import com.ibm.sk.ff.gui.common.objects.operations.CloseData;
import com.ibm.sk.ff.gui.common.objects.operations.CreateGameData;
import com.ibm.sk.ff.gui.common.objects.operations.InitMenuData;
import com.ibm.sk.ff.gui.common.objects.operations.ResultData;
import com.ibm.sk.ff.gui.common.objects.operations.ScoreData;
import com.sun.istack.internal.logging.Logger;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler {
	
	private final Logger LOGGER = Logger.getLogger(RequestHandler.class);
	private final Facade FACADE;
	
	public RequestHandler(Facade facade) {
		FACADE = facade;
	}
	
	@Override
	public void handle(HttpExchange paramHttpExchange) throws IOException {
		String path = paramHttpExchange.getRequestURI().getPath();
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		String [] swp = path.split("/");
		GUIOperations operation = createGUIOperation(swp);
		
		if (operation.equals(GUIOperations.EVENT_POLL)) {
			String response = callEventPoll();
			paramHttpExchange.sendResponseHeaders(200, 0);
			paramHttpExchange.setAttribute("Content-Length", Integer.valueOf(response.length()));
			paramHttpExchange.getResponseBody().write(response.getBytes());
			paramHttpExchange.close();
		} else {
			String json = readRequestBody(paramHttpExchange.getRequestBody());
			
			handle(operation, (swp.length > 1) ? swp[1] : null, json);
			
			paramHttpExchange.sendResponseHeaders(200, 0);
		}
	}
	
	private void handle(GUIOperations operation, String leftoverURL, String json) {
		switch (operation) {
		case SHOW_INIT_MENU:	showInitMenu(leftoverURL, json);	break;
		case CREATE_GAME: 		callCreateGame(leftoverURL, json);	break;
		case SET:				callSet(leftoverURL, json);			break;
		case REMOVE:			callRemove(leftoverURL, json);		break;
		case SCORE :			callScore(leftoverURL, json);		break;
		case SHOW_RESULT:		callShowResult(null, json);			break;
		case CLOSE:				callClose(leftoverURL, json);		break;
		default: break;
		}
	}
	
	private String callEventPoll() {
		return FACADE.getEvents();
	}
	
	private void showInitMenu(String url, String json) {
		log("Showing init menu."); 
		FACADE.initMenu(Mapper.INSTANCE.jsonToPojo(json, InitMenuData.class));
	}
	
	private void callCreateGame(String url, String json) {
		log("Creating window.");
		FACADE.createGame(Mapper.INSTANCE.jsonToPojo(json, CreateGameData.class));
	}
	
	private void callSet(String url, String json) {
		switch (GUIObjectTypes.forValue(url)) {
		case ANT: log("Adding new ant."); FACADE.set(Mapper.INSTANCE.jsonToPojo(json, GAntObject.class)); break;
		case ANT_FOOD: log("Adding new antfood."); FACADE.set(Mapper.INSTANCE.jsonToPojo(json, GAntFoodObject.class)); break;
		case FOOD: log("Adding new food."); FACADE.set(Mapper.INSTANCE.jsonToPojo(json, GFoodObject.class)); break;
		case HILL: log("Adding new hill."); FACADE.set(Mapper.INSTANCE.jsonToPojo(json, GHillObject.class)); break;
		}
	}
	
	private void callRemove(String url, String json) {
		switch (GUIObjectTypes.forValue(url)) {
		case ANT: log("Removing ant."); FACADE.remove(Mapper.INSTANCE.jsonToPojo(json, GAntObject.class)); break;
		case ANT_FOOD: log("Removing antfood."); FACADE.remove(Mapper.INSTANCE.jsonToPojo(json, GAntFoodObject.class)); break;
		case FOOD: log("Removing food."); FACADE.remove(Mapper.INSTANCE.jsonToPojo(json, GFoodObject.class)); break;
		case HILL: log("Removing hil			StringBuilder sb = new StringBuilder();l."); FACADE.remove(Mapper.INSTANCE.jsonToPojo(json, GHillObject.class)); break;
		}
	}
	
	private void callScore(String url, String json) {
		log("Showing score.");
		FACADE.score(Mapper.INSTANCE.jsonToPojo(json, ScoreData.class));
	}
	
	private void callShowResult(String url, String json) {
		log("Showing results.");
		FACADE.showResults(Mapper.INSTANCE.jsonToPojo(json, ResultData.class));
	}
	
	private void callClose(String url, String json) {
		log("Closing window.");
		FACADE.close(Mapper.INSTANCE.jsonToPojo(json, CloseData.class));
	}
	
	private GUIOperations createGUIOperation(String [] path) {
		GUIOperations ret = null;

		if (path.length >= 1) {
			ret = GUIOperations.forValue(path[0].trim());
		}
		
		return ret;
	}
	
	private String readRequestBody(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		if (in != null && in.available() > 0) {
			byte [] buffer = new byte [1024];
			int read;
			
			while ((read = in.read(buffer)) > 0) {
				sb.append(new String(buffer, 0, read));
			}
		}

		return sb.toString();
	}
	
	private void log(String message) {
		LOGGER.log(Level.INFO, message);
	}

}
