package org.xiao.rss;//******************************************************************************
//**  RSS Media
//******************************************************************************

import org.xiao.rss.Parser;

/**
 *   Used to represent media associated with an RSS Item.
 *
 ******************************************************************************/

public class Media {

    private String type;
    private java.net.URL url;
    private String credit;
    private String description;

    
  //**************************************************************************
  //** Constructor
  //**************************************************************************
    protected Media(org.w3c.dom.Node[] nodes){
        
        java.net.URL thumbnail = null;
        String thumbnailType = null;
        java.net.URL content = null;
        String contentType = null;
        
        for (org.w3c.dom.Node node : nodes){
            String nodeName = node.getNodeName();
            if (nodeName.endsWith("credit")){
                credit = org.xiao.rss.Parser.getNodeValue(node).trim();
            }
            else if (nodeName.endsWith("description")){
                description = org.xiao.rss.Parser.getNodeValue(node).trim();
            }
            else if (nodeName.endsWith("thumbnail")){
                String link = org.xiao.rss.Parser.getAttributeValue(node,"url").trim();
                if (link.length()>0){
                    try{ 
                        thumbnail = new java.net.URL(link); 
                        thumbnailType = org.xiao.rss.Parser.getAttributeValue(node,"type").trim();
                    }
                    catch(Exception e){}
                }
            }
            else if (nodeName.endsWith("content")){
                String link = org.xiao.rss.Parser.getAttributeValue(node,"url").trim();
                if (link.length()>0){
                    try{ 
                        content = new java.net.URL(link); 
                        contentType = org.xiao.rss.Parser.getAttributeValue(node,"type").trim();
                    }
                    catch(Exception e){}
                }
            }
            else if (nodeName.equals("media:group")){
                org.w3c.dom.Node child = node.getFirstChild();
                String link = org.xiao.rss.Parser.getAttributeValue(child,"url").trim();
                if (link.length()>0){
                    try{
                        content = new java.net.URL(link);
                        contentType = org.xiao.rss.Parser.getAttributeValue(node,"type").trim();
                    }
                    catch(Exception e){}
                }
            }
        }
        
        if (content!=null){ 
            url = content;
            type = contentType;
        }
        else{ 
            url = thumbnail;
            type = thumbnailType;
        }
    }

    
  //**************************************************************************
  //** Constructor
  //**************************************************************************
  /** Used to parse a media content node associated with an RSS entry.
   */
    protected Media(org.w3c.dom.Node node) {
        type = org.xiao.rss.Parser.getAttributeValue(node,"type").trim();

      //Parse url
        String link = org.xiao.rss.Parser.getAttributeValue(node,"url").trim();
        if (link.length()>0){
            try{ url = new java.net.URL(link); }
            catch(Exception e){}
        }

        org.w3c.dom.NodeList nodeList = node.getChildNodes();
        for (int i=0; i<nodeList.getLength(); i++){
            node = nodeList.item(i);
            if (node.getNodeType()==1){
                String nodeName = node.getNodeName();
                if (nodeName.endsWith("credit")){
                    credit = org.xiao.rss.Parser.getNodeValue(node).trim();
                }
                else if (nodeName.endsWith("description")){
                    description = Parser.getNodeValue(node).trim();
                }
            }
        }
    }
    

    

    public String getType(){ return type; }
    public String getCredit(){ return credit; }
    public String getDescription(){ return description; }
    public java.net.URL getLink(){ return url; }

    public String toString(){

        StringBuffer out = new StringBuffer();
        String br = "\r\n";

        String mimeType = getType();
        if (mimeType.contains("/")) mimeType = mimeType.substring(0, mimeType.indexOf("/"));
        mimeType = mimeType.substring(0,1).toUpperCase() + mimeType.substring(1);
        out.append(mimeType + ": " + getLink() + br);
        out.append("Description: " + getDescription() + br);
        out.append("Credit: " + getCredit() + br);
        return out.toString();

    }

}