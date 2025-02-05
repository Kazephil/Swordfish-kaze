/*******************************************************************************
 * Copyright (c) 2007 - 2025 Maxprograms.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 1.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-v10.html
 *
 * Contributors:
 *     Maxprograms - initial API and implementation
 *******************************************************************************/

package com.maxprograms.swordfish.models;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.maxprograms.converters.Utils;
import com.maxprograms.languages.Language;
import com.maxprograms.languages.LanguageUtils;
import com.maxprograms.swordfish.ProjectsHandler;

public class Project implements Comparable<Project> {

	public static final int NEW = 0;

	private String id;
	private String description;
	private int status;
	private String client;
	private String subject;
	private Language sourceLang;
	private Language targetLang;
	private LocalDate creationDate;
	private List<SourceFile> files;
	private String memory;
	private String glossary;
	private String xliff;

	public Project(String id, String description, int status, Language sourceLang, Language targetLang, String client,
			String subject, String memory, String glossary, LocalDate creationDate) {
		this.id = id;
		this.description = description;
		this.status = status;
		this.sourceLang = sourceLang;
		this.targetLang = targetLang;
		this.client = client;
		this.subject = subject;
		this.creationDate = creationDate;
		this.memory = memory;
		this.glossary = glossary;
	}

	public String getXliff() {
		return xliff;
	}

	public void setXliff(String xliff) {
		this.xliff = xliff;
	}

	public Project(JSONObject json) throws IOException, JSONException, SAXException, ParserConfigurationException {
		id = json.getString("id");
		description = json.getString("description");
		status = json.getInt("status");
		sourceLang = LanguageUtils.getLanguage(json.getString("sourceLang"));
		targetLang = LanguageUtils.getLanguage(json.getString("targetLang"));
		client = json.has("client") ? json.getString("client") : "";
		subject = json.has("subject") ? json.getString("subject") : "";
		creationDate = LocalDate.parse(json.getString("creationDate"));
		files = new Vector<>();
		JSONArray filesArray = json.getJSONArray("files");
		for (int i = 0; i < filesArray.length(); i++) {
			SourceFile sourceFile = new SourceFile(filesArray.getJSONObject(i));
			if (!files.contains(sourceFile)) {
				files.add(sourceFile);
			}
		}
		xliff = json.getString("xliff");
		if (!new File(xliff).isAbsolute()) {
			xliff = Utils.getAbsolutePath(ProjectsHandler.getWorkFolder(), xliff);
		}
		memory = json.getString("memory");
		glossary = json.getString("glossary");
	}

	public JSONObject toJSON() throws IOException, JSONException {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("description", description);
		json.put("status", status);
		json.put("sourceLang", sourceLang.getCode());
		json.put("targetLang", targetLang.getCode());
		json.put("client", client);
		json.put("subject", subject);
		json.put("creationDate", creationDate.toString());
		JSONArray filesArray = new JSONArray();
		Iterator<SourceFile> it = files.iterator();
		while (it.hasNext()) {
			filesArray.put(it.next().toJSON());
		}
		json.put("files", filesArray);
		json.put("xliff", Utils.getRelativePath(ProjectsHandler.getWorkFolder().getAbsolutePath(), xliff));
		json.put("memory", memory);
		json.put("glossary", glossary);
		return json;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Language getSourceLang() {
		return sourceLang;
	}

	public void setSourceLang(Language sourceLang) {
		this.sourceLang = sourceLang;
	}

	public Language getTargetLang() {
		return targetLang;
	}

	public void setTargetLang(Language targetLang) {
		this.targetLang = targetLang;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public List<SourceFile> getFiles() {
		return files;
	}

	public void setFiles(List<SourceFile> files) {
		this.files = files;
	}

	public void setFiles(JSONArray array) {
		files = new Vector<>();
		for (int i = 0; i < array.length(); i++) {
			files.add(new SourceFile(array.getJSONObject(i)));
		}
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getGlossary() {
		return glossary;
	}

	public void setGlossary(String glossary) {
		this.glossary = glossary;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Override
	public int compareTo(Project o) {
		return creationDate.compareTo(o.getCreationDate());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Project p) {
			return id.equals(p.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
