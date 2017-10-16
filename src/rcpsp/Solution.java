/*
	rcpsp-framework
	Copyright (C) 2017 Fabien Lehuede / Damien Prot / Axel Grimault

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License along
	with this program; if not, write to the Free Software Foundation, Inc.,
	51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package rcpsp;

import java.io.PrintStream;
import java.util.Arrays;

/**
 * 
 * This class models a RCPSP Solution.
 * 
 * @author Fabien Lehuede / Damien Prot / Axel Grimault 2017
 * 
 */
public class Solution {

	// --------------------------------------------
	// --------------- ATTRIBUTS ------------------
	// --------------------------------------------

	/**
	 * Solution stored in an array. Element at the j-th position represents
	 * activity j and contains the begin time of activity j.
	 * 
	 * ###DO NOT MODIFY###
	 */
	protected Integer[] m_beginTimeActivity;

	/**
	 * Value of the objective, i.e., the makespan of all activities
	 * This value should be maintain properly while the solution is modified.
	 * 
	 * For computational requirement, update this attribute with method {@link #evaluate()} but it's costly.
	 */
	protected int m_objectiveValue = 0;

	/** 
	 * Sum of resources used at each step time.
	 * For a solution, computational of each resource is done by method {@link #validate()}.
	 * 
	 * For performance reasons, it is better to update this element iteratively
	 * with method {@link #setConsumptionResource(int i, int date, int val)}.
	 * 
	 * This iteratively update is done in {@link #addActivity(int j, int time)}.
	 */
	private int[][] m_consumptionResource;


	/** Data of the problem associated with the solution */
	protected Instance m_instance;

	/** Error code returned by {@link #validate()} */
	protected String m_error = "";


	// --------------------------------------------
	// ------------ GETTERS AND SETTERS -----------
	// --------------------------------------------

	/**
	 * @return Array of begin times of activities
	 */
	public Integer[] getBeginTimeActivities()
	{
		return m_beginTimeActivity;
	}

	/**
	 * @return Objective value (i.e. makespan)
	 */
	public int getObjectiveValue()
	{
		return m_objectiveValue;
	}

	/**
	 * Set the cost of the solution.
	 * 
	 * @param value : new objective value
	 */
	public void setObjectiveValue(int value)
	{
		this.m_objectiveValue = value;
	}

	/**
	 * @return An array representing the consumption of each resource
	 */
	public int[][] getConsumptionResources()
	{
		return m_consumptionResource;
	}

	/**
	 * @return An array representing the consumption of each resource
	 */
	public void setConsumptionResources(int[][] consumptionResource)
	{
		this.m_consumptionResource = consumptionResource;
	}

	/**
	 * @param i Index of resource
	 * @param time The time for which one wants to know the consumption
	 * @return The consumption of resource i at time time
	 */
	public int getConsumptionResource(int i, int time)
	{
		return getConsumptionResources()[i][time]; 
	}

	/**
	 * Initialize the consumption of the resource
	 *
	 * @param i Index of resource
	 * @param time The time for which one wants to set the consumption
	 * @param value The value of the consumption
	 */
	public void setConsumptionResource(int i, int time, int value)
	{
		this.m_consumptionResource[i][time] = value;
	}

	/**
	 * @return Returns a pointer to the data of the problem associated with the solution.
	 */
	public Instance getInstance() {
		return m_instance;
	}

	/**
	 * @return Error code returned by {@link #validate()}
	 */
	public String getError() {
		return m_error;
	}

	// -------------------------------------
	// ------------ CONSTRUCTOR ------------
	// -------------------------------------

    /**
     * Creates an object of the class Solution for the problem data loaded in Instance.
     * 
     * @param instance The instance of the problem.
     */
	public Solution(Instance instance) throws Exception
	{ 
		m_instance = instance;
		int nbActivities = instance.getNbActivities();
		int nbResources = instance.getNbResources();
		m_beginTimeActivity = new Integer[nbActivities];
		Arrays.fill(m_beginTimeActivity, null);

		// maximum horizon time : the sum of all activities duration
		int maxHorizonTime = 0;
		for(int j = 0; j < nbActivities; j++)
		{
			maxHorizonTime += instance.getDurationActivity(j);
		}

		setConsumptionResources(new int[nbResources][maxHorizonTime]);
		for(int[] row : getConsumptionResources())
		{
			Arrays.fill(row, 0);
		}		
	}

	// -------------------------------------
	// -------------- METHODS --------------
	// -------------------------------------


	/** 
	 * Overload of method clone of class <code>Object</code>.
	 * 
	 * @return A copy of the solution.
	 */
	public Solution clone()
	{
		Solution solution = null;
		try
		{
			solution = new Solution(m_instance);
		} catch (Exception e)
		{ 
			e.printStackTrace();
		}
		solution.m_objectiveValue = m_objectiveValue;
		solution.m_beginTimeActivity = Arrays.copyOf(m_beginTimeActivity, m_instance.getNbActivities());
		solution.setConsumptionResources(Arrays.copyOf(getConsumptionResources(),m_instance.getNbResources()));
		solution.m_error = new String(m_error); 
		return solution;	  
	}

	/**
	 * Add activity j at time time (shift the activity if it is already scheduled).
	 * Update the objective value and all resources used.
	 * 
	 * @param j index of the activity to add
	 * @param time begin time of the activity
	 * @throws Exception Throw an exception if activity j is not a valid object.
	 */
	public void addActivity(int j, int time) throws Exception
	{
		int nbActivities = m_instance.getNbActivities();
		if ((j < 0) || (j >= nbActivities))
			throw new Exception("Error: " + j + " is not an index of activity between 0 and " + (nbActivities - 1));

		// If activity was already set to a time, we removed it
		if(m_beginTimeActivity[j] != null)
		{
			removeActivity(j);
		}

		// Update the solution
		m_beginTimeActivity[j] = time;

		// Update the objective value
		m_objectiveValue = Math.max(m_objectiveValue, time + m_instance.getDurationActivity(j));

		// Update consumption of resources
		for(int t = time; t < time + m_instance.getDurationActivity(j); t++)
		{
			for(int i = 0; i < m_instance.getNbResources(); i++)
			{
				getConsumptionResources()[i][t] += m_instance.getConsumptionResourceActivity(i, j);
			}
		}
	}

	/**
	 * Remove activity j (do nothing if the activity is not scheduled).
	 * Update the objective value and all resources used.
	 * 
	 * @param j index of the activity to add
	 * @throws Exception Throw an exception if activity j is not a valid object.
	 */
	public void removeActivity(int j) throws Exception
	{
		int nbActivities = m_instance.getNbActivities();
		if ((j < 0) || (j >= nbActivities))
			throw new Exception("Error: " + j + " is not an index of activity between 0 and " + (nbActivities - 1));

		if (m_beginTimeActivity[j] != null)
		{
			// Update the objective value
			evaluate();

			int time = m_beginTimeActivity[j];
			// Update consumption of resources
			for (int t = time; t < time + m_instance.getDurationActivity(j); t++)
			{
				for (int i = 0; i < m_instance.getNbResources(); i++)
				{
					getConsumptionResources()[i][t] -= m_instance.getConsumptionResourceActivity(i, j);
				}
			}

			// Update begin time
			m_beginTimeActivity[j] = null;
		}
	} 


	/**
	 * Check if adding activity j at time time does not violate resource constraints and precedence constraints
	 * (regarding to the current solution).
	 * 
	 * @param j index of the activity to add
	 * @param time begin time of the activity
	 * @return true if it is possible to add the task, false otherwise
	 * @throws Exception Throw an exception if activity j is not a valid object.
	 * 
	 */
	public boolean isPossibleToAdd(int j, int time) throws Exception
	{
		int nbActivities = m_instance.getNbActivities();
		if ((j < 0) || (j >= nbActivities))
			throw new Exception("Error: " + j + " is not an index of activity between 0 and " + (nbActivities - 1));

		// Check resources constraints
		int nbResources = m_instance.getNbResources();
		int durationActivity = m_instance.getDurationActivity(j);
		for(int t = time; t < time + durationActivity; t++)
		{
			for(int i = 0; i < nbResources; i++)
			{
				if(getConsumptionResources()[i][t] + m_instance.getConsumptionResourceActivity(i,j) > m_instance.getCapacityResource(i))
				{
					return false;
				}
			}
		}

		// Check precedence constraints
		for(int i = 0; i < nbActivities; i++)
		{
			// If i is a predecessor of j, it must be scheduled before
			if(m_instance.getPrecedence(i, j))
			{
				if (m_beginTimeActivity[i] != null)
				{
					if(m_beginTimeActivity[i] + m_instance.getDurationActivity(i) > time)
					{
						return false;
					}
				}
			}
			// If i is a successor of j, it must be scheduled after
			if(m_instance.getPrecedence(j,i))
			{
				if(m_beginTimeActivity[i] != null)
				{
					if(m_beginTimeActivity[i] < m_instance.getDurationActivity(j) + time)
					{
						return false;
					}
				}
			}
		}

		return true;
	}


	/**
	 * Compute the objective value
	 * Update the attribute {@link #m_objectiveValue}
	 * 
	 * @throws Exception
	 */ 
	public int evaluate() throws Exception
	{
		m_objectiveValue = 0;
		int nbActivities = m_instance.getNbActivities();
		for(int j = 0; j < nbActivities; j++)
		{
			if (m_beginTimeActivity[j] != null)
			{
				m_objectiveValue = Math.max(m_objectiveValue,m_instance.getDurationActivity(j) + m_beginTimeActivity[j]);
			}
		}
		return m_objectiveValue;
	}


	/**
	 * Check if the solution is feasible solution for RCPSP.
	 * Tests are following:
	 *  - compute objective value and compute resources consumption
	 *  - check if each resource is respected
	 *  
	 * Error messages are available through command {@link #getError()}
	 * 
	 * @return Return <code>true</code> if solution is valid, <code>false</code> otherwise.
	 * @throws Exception
	 */
	public boolean validate() throws Exception {
		boolean result = true;
		m_error = "";
		evaluate();

		int nbActivities = m_instance.getNbActivities();
		int nbRessources = m_instance.getNbResources();
		// Update resources consumption
		for(int[] row: getConsumptionResources())
		{
			Arrays.fill(row, 0);
		}	
		for(int j = 0; j <nbActivities; j++)
		{
			if(m_beginTimeActivity[j] == null)
			{
				m_error+= "Error: activity " + j + " is not scheduled\n";
				result = false;
			}
			else
			{
				for(int t = m_beginTimeActivity[j]; t < m_beginTimeActivity[j] + m_instance.getDurationActivity(j); t++)
				{
					for(int i = 0; i < nbRessources; i++)
					{
						getConsumptionResources()[i][t] += m_instance.getConsumptionResourceActivity(i, j);
					}
				}
			}
		}

		int dureeMax = getConsumptionResources()[0].length;
		for(int t = 0; t < dureeMax; t++)
		{
			for(int i = 0; i < nbRessources ; i++)
			{
				if(getConsumptionResources()[i][t] > m_instance.getCapacityResource(i))
				{
					m_error += "Error: resource " + i + " at time" + t + " exceeds capacity.\n";
					result = false;
				}
			}
		}


		// Precedence constraints
		for(int j = 0; j <nbActivities; j++)
		{
			if(m_beginTimeActivity[j] != null)
			{
				for (int i = j+1; i < nbActivities; i++)
				{
					// If i is a predecessor of j, it must be scheduled before
					if(m_instance.getPrecedence(i, j))
					{
						if(m_beginTimeActivity[i] != null)
						{
							if(m_beginTimeActivity[i] + m_instance.getDurationActivity(i) > m_beginTimeActivity[j])
								return false;
						}
					}
					// If i is a successor of j, it must be scheduled after
					if(m_instance.getPrecedence(j,i))
					{
						if(m_beginTimeActivity[i] != null)
						{
							if(m_beginTimeActivity[i] < m_instance.getDurationActivity(j) + m_beginTimeActivity[j])
								return false;
						}
					}
				}
			}
		}


		if(result)
		{
			m_error += "Solution is feasible.";
		}
		else
		{
			m_error += "Solution is not feasible.";
		}
		return result;
	}

	/**
	 * Print the solution to the stream <code>out</code>.
	 * 
	 * @param out Output
	 */
	public void print(PrintStream out)
	{
		out.println("-- RCPSP Solution -- ");
		out.println("Objective value: " + m_objectiveValue);
		out.println("Activity schedule: ");
		for(int j = 0; j < m_instance.getNbActivities(); j++)
		{
			out.print("Activity " + j + ": " + m_beginTimeActivity[j]+"\n");
		}		
		out.println("---------------------");
	}

}
