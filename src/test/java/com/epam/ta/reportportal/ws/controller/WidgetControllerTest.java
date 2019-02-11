package com.epam.ta.reportportal.ws.controller;

import com.epam.ta.reportportal.dao.WidgetRepository;
import com.epam.ta.reportportal.entity.widget.Widget;
import com.epam.ta.reportportal.ws.BaseMvcTest;
import com.epam.ta.reportportal.ws.model.EntryCreatedRS;
import com.epam.ta.reportportal.ws.model.widget.ContentParameters;
import com.epam.ta.reportportal.ws.model.widget.WidgetRQ;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author <a href="mailto:ihar_kahadouski@epam.com">Ihar Kahadouski</a>
 */
@Sql("/db/shareable/shareable-fill.sql")
public class WidgetControllerTest extends BaseMvcTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WidgetRepository widgetRepository;

	@Test
	public void createWidgetPositive() throws Exception {
		WidgetRQ rq = new WidgetRQ();
		rq.setName("widget");
		rq.setDescription("description");
		rq.setWidgetType("oldLineChart");
		ContentParameters contentParameters = new ContentParameters();
		contentParameters.setContentFields(Arrays.asList("number", "name", "user", "statistics$defects$automation_bug$AB002"));
		contentParameters.setItemsCount(50);
		rq.setFilterIds(Collections.singletonList(3L));
		rq.setContentParameters(contentParameters);
		rq.setShare(true);
		final MvcResult mvcResult = mockMvc.perform(post(DEFAULT_PROJECT_BASE_URL + "/widget").with(token(oAuthHelper.getDefaultToken()))
				.content(objectMapper.writeValueAsBytes(rq))
				.contentType(APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
		final EntryCreatedRS entryCreatedRS = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), EntryCreatedRS.class);
		final Optional<Widget> optionalWidget = widgetRepository.findById(entryCreatedRS.getId());
		assertTrue(optionalWidget.isPresent());
		assertEquals("widget", optionalWidget.get().getName());
		assertEquals("description", optionalWidget.get().getDescription());
	}

	@Test
	public void getWidgetPositive() throws Exception {
		mockMvc.perform(get(DEFAULT_PROJECT_BASE_URL + "/widget/10").with(token(oAuthHelper.getDefaultToken()))).andExpect(status().isOk());
	}

	@Test
	public void updateWidgetPositive() throws Exception {
		final WidgetRQ rq = new WidgetRQ();
		rq.setName("updated");
		rq.setDescription("updated");
		rq.setWidgetType("activityStream");
		rq.setShare(false);
		final ContentParameters contentParameters = new ContentParameters();
		contentParameters.setContentFields(Arrays.asList("number", "start_time", "user"));
		contentParameters.setItemsCount(50);
		rq.setContentParameters(contentParameters);
		mockMvc.perform(put(DEFAULT_PROJECT_BASE_URL + "/widget/12").with(token(oAuthHelper.getDefaultToken()))
				.content(objectMapper.writeValueAsBytes(rq))
				.contentType(APPLICATION_JSON)).andExpect(status().isOk());
		final Optional<Widget> optionalWidget = widgetRepository.findById(12L);
		assertTrue(optionalWidget.isPresent());
		assertEquals("updated", optionalWidget.get().getName());
		assertEquals("updated", optionalWidget.get().getDescription());
	}

	@Test
	public void updateNonExistingWidget() throws Exception {
		WidgetRQ rq = new WidgetRQ();
		rq.setName("name");
		rq.setWidgetType("oldLineChart");
		rq.setShare(false);
		mockMvc.perform(put(DEFAULT_PROJECT_BASE_URL + "/widget/100").with(token(oAuthHelper.getDefaultToken()))
				.content(objectMapper.writeValueAsBytes(rq))
				.contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void getSharedWidgetsListPositive() throws Exception {
		mockMvc.perform(get(DEFAULT_PROJECT_BASE_URL + "/widget/shared").with(token(oAuthHelper.getSuperadminToken())))
				.andExpect(status().isOk());
	}

	@Test
	public void searchSharedWidgetsListPositive() throws Exception {
		mockMvc.perform(get(DEFAULT_PROJECT_BASE_URL + "/widget/shared/search?term=ch").with(token(oAuthHelper.getSuperadminToken())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", Matchers.hasSize(4)));
	}

}