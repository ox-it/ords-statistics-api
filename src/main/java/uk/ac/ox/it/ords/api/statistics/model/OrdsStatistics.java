/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ox.it.ords.api.statistics.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
