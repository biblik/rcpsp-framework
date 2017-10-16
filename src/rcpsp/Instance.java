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

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Instance class allows to create an object that contains the data stored
 * in a rcpsp file.
 * 
 * The class is created through its constructor that takes the data file as
 * parameter. The data file is read and the data are stored in the Instance
 * object. The data can then be access calling the object methods.
 * 
 * @warning Do not modify this class.
 * @author Fabien Lehuede / Damien Prot / Axel Grimault 2017
 * 
 */
public class Instance {

	// ---------------------------------------------
	// --------------- ATTRIBUTES ------------------
	// ---------------------------------------------

	/** Number of resources */
	private int m_nbResources;

	/** Number of activities */
	private int m_nbActivities;

	/** Capacity of each resource */
	private Integer[] m_capacityResource;

	/** Duration of each activity */
	private Integer[] m_durationActivity;

	/** Consumption of each resource for each activity */
	private Integer[][] m_consumptionResourceActivity;

	/** Precedences matrix between activities **/
	private boolean[][] m_precedences;

	/** Direct Precedences matrix between activities 
	 *  if m_directPrecedences[i][j] = true, activity i must be finished before activity j begins and there is no activity k such
	 *  that activity i must be finished before activity k and activity k must be finished before activity j **/
	private boolean[][] m_directPrecedences;

	/** Name of the file corresponding to the instance */
	private String m_fileName;

	// --------------------------------------------
	// ------------ GETTERS AND SETTERS -----------
	// --------------------------------------------

	/** 
	 * @return The number of resources in the problem
	 */
	public int getNbResources() {
		return m_nbResources;
	}

	/** 
	 * @return The number of activities in the problem
	 */
	public int getNbActivities() {
		return m_nbActivities;
	}

	/**
	 * @param i Index of the resource (index must be between 0 and the number of resources -1).
	 * @return The capacity of resource i.
	 * @throws Exception Throw an exception if index i is not a valid resource.
	 */
	public int getCapacityResource(int i) throws Exception {
		if ((i < 0) || (i >= m_nbResources))
			throw new Exception("Error: " + i + " is not an index of resource between 0 and " + (m_nbResources - 1));
		return m_capacityResource[i];
	}

	/**
	 * @param j Index of the activity (index must be between 0 and the number of activities -1).
	 * @return The duration of activity j.
	 * @throws Exception Throw an exception if activity j is not a valid object.
	 */
	public int getDurationActivity(int j) throws Exception {
		if ((j < 0) || (j >= m_nbActivities))
			throw new Exception("Error: " + j + " is not an index of activity between 0 and " + (m_nbActivities - 1));
		return m_durationActivity[j];
	}

	/**
	 * @param i Index of the resource (index must be between 0 and the number of resources -1).
	 * @param j Index of the activity (index must be between 0 and the number of activities -1).
	 * @return The consumption of resource i for activity j.
	 * @throws Exception Throw an exception if indices i and j are not valid.
	 **/
	public Integer getConsumptionResourceActivity(int i, int j) throws Exception {
		if ((i < 0) || (i >= m_nbResources))
			throw new Exception("Error: " + i + " is not an index of resource between 0 and " + (m_nbResources - 1));
		if ((j < 0) || (j >= m_nbActivities))
			throw new Exception("Error: " + j + " is not an index of activity between 0 and " + (m_nbActivities - 1));
		return m_consumptionResourceActivity[i][j];
	}

	/**
	 * @param a Index of the activity (index must be between 0 and the number of activities -1).
	 * @param b Index of the activity (index must be between 0 and the number of activities -1).
	 * @return True if activity a must be finished before activity b begins.
	 * @throws Exception Throw an exception if indices i and j are not valid.
	 **/
	public boolean getPrecedence(int a, int b) throws Exception {
		if ((a < 0) || (a >= m_nbActivities))
			throw new Exception("Error: " + a + " n\'is not an index of activity between 0 and " + (m_nbActivities - 1));
		if ((b < 0) || (b >= m_nbActivities))
			throw new Exception("Error: " + b + " n\'is not an index of activity between 0 and " + (m_nbActivities - 1));
		return m_precedences[a][b];
	}

	/**
	 * @param a Index of the activity (index must be between 0 and the number of activities -1).
	 * @param b Index of the activity (index must be between 0 and the number of activities -1).
	 * @return True if activity a must be finished before activity b begins and it is a direct predecessor.
	 * @throws Exception Throw an exception if indices i and j are not valid.
	 **/
	public boolean getDirectPrecedence(int a, int b) throws Exception {
		if ((a < 0) || (a >= m_nbActivities))
			throw new Exception("Error: " + a + " n\'is not an index of activity between 0 and " + (m_nbActivities - 1));
		if ((b < 0) || (b >= m_nbActivities))
			throw new Exception("Error: " + b + " n\'is not an index of activity between 0 and " + (m_nbActivities - 1));
		return m_directPrecedences[a][b];
	}

	/**
	 * @return The name of the file.
	 */
	public String getFileName() {
		return m_fileName;
	}

	// -------------------------------------
	// ------------ CONSTRUCTOR ------------
	// -------------------------------------

	/**
	 * Constructor: this method creates an object of class Instance. It calls
	 * the read method to load the data file given as parameter.
	 * 
	 * @param fileName
	 *            instance file
	 * @throws IOException
	 *             Returns an error when a problem is met reading the data file.
	 */
	public Instance(String fileName) throws IOException {
		m_fileName = fileName;
		read();
	}

	// -------------------------------------
	// -------------- METHODS --------------
	// -------------------------------------

	/** 
	 * Read the instance file
	 */
	private void read() throws IOException {

		File mfile = new File(m_fileName);
		if (!mfile.exists())
		{
			throw new IOException("The instance file : " + m_fileName + " does not exist.");
		}
		Scanner sc = new Scanner(mfile);

		String line = sc.nextLine();
		


		// Read the number of activities
		while (!line.contains("jobs"))
		{
			line = sc.nextLine();
		}
		Scanner lineSc = new Scanner(line);
		String s;
		do{
			s = lineSc.next();
		}while(!s.contains(":"));
		m_nbActivities = lineSc.nextInt();

		m_precedences = new boolean[m_nbActivities][m_nbActivities];
		m_directPrecedences = new boolean[m_nbActivities][m_nbActivities];
		m_durationActivity = new Integer[m_nbActivities];

		lineSc.close();

		// Read the number of resources
		do
		{
			line = sc.nextLine();
		}while(!line.contains("renewable"));
		lineSc = new Scanner(line);
		do
		{
			s = lineSc.next();
		}while(!s.contains(":"));
		m_nbResources = lineSc.nextInt();

		m_capacityResource = new Integer[m_nbResources];
		m_consumptionResourceActivity = new Integer[m_nbResources][m_nbActivities];

		// Read precedences
		do
		{
			line = sc.nextLine();
		}while(!line.contains("jobnr"));

		for(int j = 0; j < m_nbActivities; j++)
		{

			line = sc.nextLine();
			lineSc = new Scanner(line);

			// Initialization
			for(int i = 0; i < m_nbActivities; i++)
			{
				m_precedences[j][i] = false;
				m_directPrecedences[j][i] = false;
			}

			lineSc.nextInt();
			lineSc.nextInt();
			int nbSucc = lineSc.nextInt();

			for(int i = 0; i < nbSucc; i++)
			{
				int succ = lineSc.nextInt() - 1; // --- Numbering starts at 1 in the instance file
				m_precedences[j][succ] = true;
				m_directPrecedences[j][succ] = true;
			}
		}


		// --- Fermeture transitive des précédences, pour une manipulation plus facile 
		// --- Pour chaque tâche j 
		for(int j = 0; j < m_nbActivities; j++)
		{
			ArrayList<Integer> listSuccessors = new ArrayList<Integer>();
			// Add direct successor in the list
			for(int i = j+1; i < m_nbActivities; i++)
			{
				if(m_precedences[j][i] == true)
				{
					listSuccessors.add(i);
				}
			}
			// Add (if needed) successors for each element in the list
			while(listSuccessors.size() != 0)
			{
				int current = listSuccessors.get(0);
				listSuccessors.remove(0);
				m_precedences[j][current] = true;
				for(int i = current+1; i < m_nbActivities; i++)
				{
					if(m_precedences[current][i] == true && !listSuccessors.contains(i))
					{
						listSuccessors.add(i);
					}
				}
			}		
		}

		// Read consumption resources
		do
		{
			line = sc.nextLine();
		}while(!line.contains("jobnr"));

		sc.nextLine();
		for(int j = 0; j < m_nbActivities; j++)
		{
			line = sc.nextLine();
			lineSc = new Scanner(line);
			lineSc.nextInt();
			lineSc.nextInt();
			m_durationActivity[j] = lineSc.nextInt();
			for(int i = 0; i < m_nbResources; i++)
			{
				m_consumptionResourceActivity[i][j] = lineSc.nextInt();
			}
		}

		lineSc.close();

		// Read capacity of resources
		do
		{
			line = sc.nextLine();
		}while(!line.contains("RESOURCEAVAILABILITIES"));

		line = sc.nextLine();
		line = sc.nextLine();
		lineSc = new Scanner(line);
		for(int i = 0; i < m_nbResources; i++)
		{
			m_capacityResource[i] = lineSc.nextInt();
		}

		sc.close();
		lineSc.close();
	}

	/**
	 * Print weights matrix on the output given as a parameter.
	 * 
	 * @param out : output stream
	 */
	public void print(PrintStream out)
	{
		out.println("Number of activities: " + m_nbActivities);
		out.println("Number of resources: " + m_nbResources);
		out.println("Resources(capacity): ");
		for(int i = 0; i < m_nbResources; i++)
		{
			out.print(" "+i+"("+m_capacityResource[i]+")");
		}
		out.println();
		out.println("Activites#resources(consumption): ");
		for(int j = 0; j < m_nbActivities; j++)
		{
			out.print("Activity "+j+" :");
			for(int i = 0; i < m_nbResources; i++)
			{
				if(m_consumptionResourceActivity[i][j] > 0)
				{
					out.print(" #"+i+"("+m_consumptionResourceActivity[i][j]+") ");
				}
			}
			out.println();
			
		}
		out.println();
	}

}
