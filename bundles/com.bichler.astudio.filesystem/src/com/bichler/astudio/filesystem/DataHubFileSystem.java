package com.bichler.astudio.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bichler.astudio.filesystem.SshFileSystem;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class DataHubFileSystem extends SshFileSystem {
	private String rootPath;

	@Override
	public boolean execCommand(String command) {
		try {
			this.chexec = (ChannelExec) session.openChannel("exec");
			this.chexec.setCommand(command);
			InputStream in = this.chexec.getInputStream();
			this.chexec.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					Logger.getLogger(getClass().getName()).log(Level.INFO, "Command: " + new String(tmp, 0, i));
				}
				if (this.chexec.isClosed()/* || this.chexec.getExitStatus() == 0 */) {
					if (in.available() > 0)
						continue;

					Logger.getLogger(getClass().getName()).log(Level.INFO,
							"Command: " + command + "! Status: " + this.chexec.getExitStatus());
					break;
				}
				if (this.chexec.getExitStatus() == 0) {
					if (in.available() > 0) {
						int i = in.read(tmp, 0, 1024);
						System.out.println("TEXT_EXIT: " + new String(tmp, 0, i));
						continue;
					}
					break;
				}
				try {
					Thread.sleep(100);
				} catch (Exception ee) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, ee.getMessage());
					return false;
				}
			}
			this.chexec.disconnect();
		} catch (JSchException | IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public void setRootPath(String path) {
		this.rootPath = path;
	}

	@Override
	public String getRootPath() {
		return this.rootPath;
	}

}
