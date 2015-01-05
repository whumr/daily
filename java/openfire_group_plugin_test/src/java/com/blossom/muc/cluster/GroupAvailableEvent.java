package com.blossom.muc.cluster;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.jivesoftware.util.cache.ClusterTask;

public class GroupAvailableEvent implements ClusterTask {

	@Override
	public void run() {

	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {

	}

	@Override
	public Object getResult() {
		return null;
	}

}
