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
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectCrate;
import com.ibm.sk.ff.gui.common.objects.gui.GUIObjectTypes;
import com.ibm.sk.ff.gui.common.objects.gui.GWarriorObject;
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

	public RequestHandler(final Facade facade) {
		this.FACADE = facade;
	}

	@Override
	public void handle(final HttpExchange paramHttpExchange) throws IOException {
		String path = paramHttpExchange.getRequestURI().getPath();
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		final String [] swp = path.split("/");
		final GUIOperations operation = createGUIOperation(swp);

		if (operation.equals(GUIOperations.EVENT_POLL)) {
			final String response = callEventPoll();
			paramHttpExchange.sendResponseHeaders(200, 0);
			paramHttpExchange.setAttribute("Content-Length", Integer.valueOf(response.length()));
			paramHttpExchange.getResponseBody().write(response.getBytes());
			paramHttpExchange.close();
		} else {
			final String json = readRequestBody(paramHttpExchange.getRequestBody());

			handle(operation, swp.length > 1 ? swp[1] : null, json);

			paramHttpExchange.sendResponseHeaders(200, 0);
			paramHttpExchange.close();
		}
	}

	protected void handle(final GUIOperations operation, final String leftoverURL, final String json) {
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
		return this.FACADE.getEvents();
	}

	private void showInitMenu(final String url, final String json) {
		log("Showing init menu.");
		this.FACADE.initMenu(Mapper.INSTANCE.jsonToPojo(json, InitMenuData.class));
	}

	private void callCreateGame(final String url, final String json) {
		log("Creating window.");
		this.FACADE.createGame(Mapper.INSTANCE.jsonToPojo(json, CreateGameData.class));
	}

	private void callSet(final String url, final String json) {
		if (url == null) {
			this.FACADE.set(Mapper.INSTANCE.jsonToPojo(json, GUIObjectCrate.class));
		} else {
			switch (GUIObjectTypes.forValue(url)) {
			case ANT:
				log("Adding new ant.");
				this.FACADE.set(Mapper.INSTANCE.jsonToPojo(json, GAntObject[].class));
				break;
			case WARRIOR:
				log("Adding new warrior.");
				this.FACADE.set(Mapper.INSTANCE.jsonToPojo(json, GWarriorObject[].class));
				break;
			case ANT_FOOD:
				log("Adding new antfood.");
				this.FACADE.set(Mapper.INSTANCE.jsonToPojo(json, GAntFoodObject[].class));
				break;
			case FOOD:
				log("Adding new food.");
				this.FACADE.set(Mapper.INSTANCE.jsonToPojo(json, GFoodObject[].class));
				break;
			case HILL:
				log("Adding new hill.");
				this.FACADE.set(Mapper.INSTANCE.jsonToPojo(json, GHillObject[].class));
				break;
			}
		}
	}

	private void callRemove(final String url, final String json) {
		if (url == null) {
			this.FACADE.remove(Mapper.INSTANCE.jsonToPojo(json, GUIObjectCrate.class));
		} else {
			switch (GUIObjectTypes.forValue(url)) {
			case ANT:
				log("Removing ant.");
				this.FACADE.remove(Mapper.INSTANCE.jsonToPojo(json, GAntObject[].class));
				break;
			case ANT_FOOD:
				log("Removing antfood.");
				this.FACADE.remove(Mapper.INSTANCE.jsonToPojo(json, GAntFoodObject[].class));
				break;
			case FOOD:
				log("Removing food.");
				this.FACADE.remove(Mapper.INSTANCE.jsonToPojo(json, GFoodObject[].class));
				break;
			case HILL:
				log("Removing hil			StringBuilder sb = new StringBuilder();l.");
				this.FACADE.remove(Mapper.INSTANCE.jsonToPojo(json, GHillObject[].class));
				break;
			}
		}
	}

	private void callScore(final String url, final String json) {
		log("Showing score.");
		this.FACADE.score(Mapper.INSTANCE.jsonToPojo(json, ScoreData.class));
	}

	private void callShowResult(final String url, final String json) {
		log("Showing results.");
		this.FACADE.showResults(Mapper.INSTANCE.jsonToPojo(json, ResultData.class));
	}

	private void callClose(final String url, final String json) {
		log("Closing window.");
		this.FACADE.close(Mapper.INSTANCE.jsonToPojo(json, CloseData.class));
	}

	protected GUIOperations createGUIOperation(final String [] path) {
		GUIOperations ret = null;

		if (path.length >= 1) {
			ret = GUIOperations.forValue(path[0].trim());
		}

		return ret;
	}

	private String readRequestBody(final InputStream in) throws IOException {
		final StringBuilder sb = new StringBuilder();

		if (in != null && in.available() > 0) {
			final byte [] buffer = new byte [1024];
			int read;

			while ((read = in.read(buffer)) > 0) {
				sb.append(new String(buffer, 0, read));
			}
		}

		return sb.toString();
	}

	private void log(final String message) {
		this.LOGGER.log(Level.INFO, message);
	}

}
