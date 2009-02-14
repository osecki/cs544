// StreamOutput.java
// This file contains generated code and will be overwritten when you rerun code generation.package com.altova.io;

package com.altova.io;

import java.io.OutputStream;

public class StreamOutput extends Output
{
	private OutputStream stream;
	
	public StreamOutput(OutputStream stream)
	{
		super(Output.IO_STREAM);
		this.stream = stream;
	}
	
	public OutputStream getStream() {return stream;}
	public void close() throws Exception {stream.close();}
}
