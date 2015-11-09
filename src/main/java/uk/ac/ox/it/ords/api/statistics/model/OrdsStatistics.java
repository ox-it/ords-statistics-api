/*
 * Copyright 2015 University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ox.it.ords.api.statistics.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Reports should specify the following:
 * Number of current live projects, and number of current live projects that are publicly accessible
 * Number of current trial projects, and number of current live projects that are publicly accessible
 * Number of live projects that have been accessed within last 12 months
 * Number of trial projects that have been accessed within last 12 months
 * Number of live projects closed over last 12 months
 * Number of trial projects closed over last 12 months
 * List of projects, their status (trial or full), their project owners, their last date of access
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "ordsstatistics")
public class OrdsStatistics implements Serializable {
    private int statsId;
    private Timestamp timeOfOperation = new Timestamp(new Date().getTime());
    private int numberOfRecordsManagedByOrds;
    private int numberOfProjectsManagedByOrds;  
    private int numberOfTrialProjects;  
    private int numberOfFullProjects;
    private int numberOfOpenProjects;
    private int numberOfClosedProjects;
    private int numberOfRecentProjects;
    private int numberOfProjectsInLastSixMonths;

	@Id
    @GeneratedValue
    public int getStatsId() {
        return statsId;
    }

    public void setStatsId(int statsId) {
        this.statsId = statsId;
    }
    
    public Timestamp getTimeOfOperation() {
        return timeOfOperation;
    }

    public void setTimeOfOperation(Timestamp timeOfOperation) {
        this.timeOfOperation = timeOfOperation;
    }

    public int getNumberOfRecordsManagedByOrds() {
        return numberOfRecordsManagedByOrds;
    }

    public void setNumberOfRecordsManagedByOrds(int numberOfRecordsManagedByOrds) {
        this.numberOfRecordsManagedByOrds = numberOfRecordsManagedByOrds;
    }
    
    public int getNumberOfProjectsManagedByOrds() {
        return numberOfProjectsManagedByOrds;
    }

    public void setNumberOfProjectsManagedByOrds(int numberOfProjectsManagedByOrds) {
        this.numberOfProjectsManagedByOrds = numberOfProjectsManagedByOrds;
    }
    
    public int getNumberOfFullProjects() {
		return numberOfFullProjects;
	}

	public void setNumberOfFullProjects(int numberOfFullProjects) {
		this.numberOfFullProjects = numberOfFullProjects;
	}

	public int getNumberOfTrialProjects() {
		return numberOfTrialProjects;
	}

	public void setNumberOfTrialProjects(int numberOfTrialProjects) {
		this.numberOfTrialProjects = numberOfTrialProjects;
	}

	public int getNumberOfOpenProjects() {
		return numberOfOpenProjects;
	}

	public void setNumberOfOpenProjects(int numberOfOpenProjects) {
		this.numberOfOpenProjects = numberOfOpenProjects;
	}

	public int getNumberOfClosedProjects() {
		return numberOfClosedProjects;
	}

	public void setNumberOfClosedProjects(int numberOfClosedProjects) {
		this.numberOfClosedProjects = numberOfClosedProjects;
	}

	public int getNumberOfRecentProjects() {
		return numberOfRecentProjects;
	}

	public void setNumberOfRecentProjects(int numberOfRecentProjects) {
		this.numberOfRecentProjects = numberOfRecentProjects;
	}

	public int getNumberOfProjectsInLastSixMonths() {
		return numberOfProjectsInLastSixMonths;
	}

	public void setNumberOfProjectsInLastSixMonths(
			int numberOfProjectsInLastSixMonths) {
		this.numberOfProjectsInLastSixMonths = numberOfProjectsInLastSixMonths;
	}
}
