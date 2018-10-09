/*
 * Copyright (C) 2018 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.ta.reportportal.core.project;

import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.OperationCompletionRS;
import com.epam.ta.reportportal.ws.model.project.AssignUsersRQ;
import com.epam.ta.reportportal.ws.model.project.UnassignUsersRQ;
import com.epam.ta.reportportal.ws.model.project.UpdateProjectRQ;
import com.epam.ta.reportportal.ws.model.project.email.ProjectEmailConfigDTO;

/**
 * Update {@link Project} request handler
 *
 * @author Hanna_Sukhadolava
 */
public interface IUpdateProjectHandler {

	/**
	 * Update specified project(projectName, customer and addInfo)
	 *
	 * @param projectName     Project Name
	 * @param updateProjectRQ Project data
	 * @param principalName   Login
	 * @return Operation result
	 * @throws ReportPortalException
	 */
	OperationCompletionRS updateProject(String projectName, UpdateProjectRQ updateProjectRQ, String principalName);

	/**
	 * Update specified project email configuration
	 *
	 * @param projectName     Project Name
	 * @param updateProjectRQ Request Data
	 * @param user            User performing that update
	 * @return Operation Result
	 */
	OperationCompletionRS updateProjectEmailConfig(String projectName, String user, ProjectEmailConfigDTO updateProjectRQ);

	/**
	 * Un-assign specified user from project
	 *
	 * @param projectName     Project Name
	 * @param modifier        Modifier
	 * @param unassignUsersRQ Request Data
	 * @return Operation Result
	 * @throws ReportPortalException
	 */
	OperationCompletionRS unassignUsers(String projectName, String modifier, UnassignUsersRQ unassignUsersRQ);

	/**
	 * Assign specified user from project
	 *
	 * @param projectName   Project Name
	 * @param modifier      Modifier
	 * @param assignUsersRQ Request Data
	 * @return Operation Result
	 * @throws ReportPortalException
	 */
	OperationCompletionRS assignUsers(String projectName, String modifier, AssignUsersRQ assignUsersRQ);

	/**
	 * Index logs for specified project
	 *
	 * @param projectName Project name
	 * @param user        User name
	 * @return Operation Result
	 */
	OperationCompletionRS indexProjectData(String projectName, String user);
}