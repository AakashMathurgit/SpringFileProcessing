package com.aakasmat.EngineProjectE6;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

import org.springframework.context.annotation.Configuration;

/*
 * This is the Request queue which will keep all the pending requests.
 */
@Configuration
public class RequestQueue {
	Set<Vector> reqQueue = new HashSet<Vector>();

	public boolean add(Vector data) {
		return reqQueue.add(data);
	}

	public Vector poll() {
		
		Vector firstElement = peek();
		reqQueue.remove(firstElement);
		return firstElement;
	}

	public Vector peek() {
		Iterator<Vector> iterator = reqQueue.iterator();
		
        if (iterator.hasNext()) 
        {
            Vector firstElement = iterator.next();
            return firstElement;
        }
        else {
            return null;
        }	
	}

	public boolean isEmpty() {
		return reqQueue.isEmpty();
	}

	public void printRequestQueue() {
		for (Vector req : reqQueue) {
			System.out.println(req.toString());
		}
	}
	
	public int size()
	{
		return reqQueue.size();
	}

}
