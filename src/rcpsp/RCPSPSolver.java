/*
	mkp-framework
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

/**
 * 
 * This class is the place where you should enter your code and from which you can create your own objects.
 * 
 * The method you must implement is {@link #solve(long)} This method is called by the programmer after loading the data.
 * 
 * The RCPSPSolver object is created by the Main class.
 * The other objects that are created in Main can be accessed through the following RCPSPSolver attributes: 
 * 	- {@link #m_instance} : the Instance object which contains the problem data
 * 	- {@link #m_solution} : the Solution object to modify. This object will store the result of the program.
 *  - {@link #m_timeLimit} : the maximum time limit (in seconds) given to the program.
 *  
 * @author Fabien Lehuede / Damien Prot / Axel Grimault 2017
 * 
 */
public class RCPSPSolver {

	// ---------------------------------------------
	// --------------- ATTRIBUTES ------------------
	// ---------------------------------------------

	/**
	 * The RCPSP Solution that will be returned by the program
	 */
	private Solution m_solution;

	/** The RCPSP data. */
	private Instance m_instance;

	/** Time given to solve the problem. */
	private long m_timeLimit;

	// --------------------------------------------
	// ------------ GETTERS AND SETTERS -----------
	// --------------------------------------------

	/** @return the problem Solution */
	public Solution getSolution()
	{
		return m_solution;
	}

	/** @return problem data */
	public Instance getInstance()
	{
		return m_instance;
	}

	/** @return Time given to solve the problem */
	public long getTimeLimit()
	{
		return m_timeLimit;
	}

	/**
	 * Initializes the problem solution with a new Solution object (the old one
	 * will be deleted).
	 * 
	 * @param sol A new solution
	 */
	public void setSolution(Solution sol)
	{
		this.m_solution = sol;
	}

	/**
	 * Sets the problem data
	 * 
	 * @param inst The Instance object which contains the data.
	 */
	public void setInstance(Instance inst)
	{
		this.m_instance = inst;
	}

	/**
	 * 
	 * @param time The maximum time limits allowed to the program (in seconds).
	 */
	public void setTime(long time)
	{
		this.m_timeLimit = time;
	}

	// -------------------------------------
	// -------------- METHODS --------------
	// -------------------------------------

	/**
	 * **TODO** Modify this method to solve the problem.
	 * 
	 * Do not print text on the standard output (eg. using <code>System.out.print()</code> or <code>System.out.println()</code>).
	 * This output is dedicated to the result analyzer that will be used to evaluate your code on multiple instances.
	 * 
	 * You can print using the error output (<code>System.err.print()</code> or <code>System.err.println()</code>).
	 * 
	 * When your algorithm terminates, make sure the attribute m_solution in this class points to the solution you want to return.
	 * 
	 * You have to make sure that your algorithm does not take more time than the time limit m_time.
	 * 
	 * @param time Time allowed (in seconds) to solve the problem.
	 * @throws Exception May return some error, in particular if some vertices index are wrong.
	 */
	public void solve(long time) throws Exception {
		m_timeLimit = time;
		long t = System.currentTimeMillis();
		long timeElapsed = 0;

		// Naive method
		int nbActivities = m_instance.getNbActivities();
		int currentProjectTime = 0;
		// Loop on activities
		// Fix activity (j+1) at the end of activity j
		// Works only if tasks are sorted w.r.t theirs precedences
		for(int j = 0; j < nbActivities; j++)
		{
			m_solution.addActivity(j, currentProjectTime);
			currentProjectTime += m_instance.getDurationActivity(j);
		}
		timeElapsed = System.currentTimeMillis() - t;
		System.err.println("Time for naive heuristic : " + timeElapsed);
		m_solution.print(System.err);
		System.err.flush();
	}

}
