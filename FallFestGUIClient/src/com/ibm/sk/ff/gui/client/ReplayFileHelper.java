package com.ibm.sk.ff.gui.client;

import static com.ibm.sk.ff.gui.common.mapper.Mapper.INSTANCE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class ReplayFileHelper {
	private ReplayFileHelper() {
	}

	public static void write(Replay replay) {
		File targetFile = new File(Config.REPLAY_FOLDER.toString() + "/" + replay.getReplayName());
		write(replay.getSteps(), targetFile);
	}

	public static void write(List<Step> steps, File file) {
		try (FileWriter writer = new FileWriter(file, true);
				BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
			bufferedWriter.write(INSTANCE.pojoToJson(steps));
		} catch (IOException e) {
			System.out.println("Failed to write to the file!" + e);
		}
	}

	public static Replay read(String fileName) {
		File targetFile = new File(Config.REPLAY_FOLDER.toString() + "/" + fileName);
		return read(targetFile);
	}

	public static Replay read(File fileName) {
		String fileContent = "";
		try (FileReader reader = new FileReader(fileName); BufferedReader bufferedReader = new BufferedReader(reader)) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				fileContent += line;
			}
			reader.close();

		} catch (IOException e) {
			System.out.println("Failed to read from the file!" + e);
		}
		Step[] toRet = INSTANCE.jsonToPojo(fileContent, Step[].class);
		return new Replay(Arrays.asList(toRet), fileName.getName());
	}

	public static String[] getAvailableReplays() {
		final File replays = new File(Config.REPLAY_FOLDER.toString());
		if (replays.exists() && replays.isDirectory()) {
			return replays.list();
		}
		return null;
	}

}