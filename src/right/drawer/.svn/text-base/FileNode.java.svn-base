package right.drawer;

import java.util.ArrayList;
import java.util.List;

public class FileNode {
	private int parentID;
	private int ID;
	private int ico;
	private String fileName;
	private boolean isExpand;
	private boolean isChecked = false;
	private List<FileNode> childFileNode = new ArrayList<FileNode>();
	private FileNode parentFileNode;
	private int level;
	public FileNode() {
	}

	public FileNode(int parentID, int ID, String fileName) {
		this.parentID = parentID;
		this.ID = ID;
		this.fileName = fileName;
		this.isExpand = false;
	}

	public int getParentID() {
		return parentID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getIco() {
		return ico;
	}

	public void setIco(int ico) {
		this.ico = ico;
	}

	public int getChildFileNodeLength() {
		return childFileNode.size();
	}

	 public int getLevel() {
	 return parentFileNode==null?0:parentFileNode.getLevel()+1;
	 }
	 public void setLevel(int level) {
	 this.level = level;
	 }
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isExpand() {
		return isExpand;
	}

	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
		// if(!isExpand){
		// for(FileNode childNode:childFileNode){
		// childNode.setExpand(isExpand);
		// }
		// }
	}

	public List<FileNode> getChildFileNode() {
		return childFileNode;
	}

	public void setChildFileNode(List<FileNode> childFileNode) {
		this.childFileNode = childFileNode;
	}

	public FileNode getParentFileNode() {
		return parentFileNode;
	}

	public void setParentFileNode(FileNode parentFileNode) {
		this.parentFileNode = parentFileNode;
	}

	public boolean isRoot() {
		return parentFileNode == null;
	}

	public boolean isLevel() {
		return childFileNode.size() == 0;
	}

	public boolean ifChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public void closeNodes(List<FileNode> nodeList) {
		for (FileNode childNode : childFileNode) {
			if (childNode.isExpand()) {
				nodeList.remove(childNode);
				childNode.closeNodes(nodeList);
			} else {
				nodeList.remove(childNode);
			}
		}
	}

	public void openNodes(int position, List<FileNode> nodeList) {
		for (FileNode childNode : childFileNode) {
			if (childNode.isExpand()) {
				nodeList.add(position + 1, childNode);
				childNode.openNodes(position + 1, nodeList);
				if(childNode.fileName.equals("Signal")){
					position =position+childNode.getChildFileNodeLength()+1;
				}
			} else {
				nodeList.add(position + 1, childNode);
				position=position+1;
			}
		}
	}

	public String getPartFileName(String partFileName) {
		if (parentFileNode != null) {
			partFileName = parentFileNode
					.getPartFileName(parentFileNode.fileName + "/"
							+ partFileName);
		}
		return partFileName;
	}

	public void checkedNodes(boolean ifChecked,
			List<FileNode> checkedFileNodeList) {
		for (FileNode childNode : childFileNode) {
			if (ifChecked) {
				checkedFileNodeList.add(childNode);
			} else {
				checkedFileNodeList.remove(childNode);
			}
			if (childNode.isExpand()) {
				childNode.setChecked(ifChecked);
				childNode.checkedNodes(ifChecked, checkedFileNodeList);
			} else {
				childNode.setChecked(ifChecked);
			}
		}
	}

	public void deleteNodes( List<FileNode> nodeList) {
		if (!isRoot()) {
			parentFileNode.childFileNode.remove(this);
		}
		nodeList.remove(this);
	}
}
