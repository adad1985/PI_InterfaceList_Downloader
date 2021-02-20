

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.sap.xi.basis.IntegratedConfiguration;
import com.sap.xi.basis.IntegratedConfigurationIn;
import com.sap.xi.basis.IntegratedConfigurationQueryIn;
import com.sap.xi.basis.IntegratedConfigurationQueryOut;
import com.sap.xi.basis.IntegratedConfigurationReadIn;
import com.sap.xi.basis.MessageHeaderID;
import com.sap.xi.basis.OutboundProcessing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.eclipse.ui.console.MessageConsoleStream;

public class ICOReader
{
  private static String apiURL = "/IntegratedConfigurationInService/IntegratedConfigurationInImplBean?wsdl=binding&mode=ws_policy";
  private static String user;
  private static String password;
  private static String url = "";
  private static IntegratedConfigurationIn port;
  
  public ICOReader() {}
  
  public List<MessageHeaderID> query() { IntegratedConfigurationQueryIn queryIn = new IntegratedConfigurationQueryIn();
    MessageHeaderID msgHdr = new MessageHeaderID();
    queryIn.setIntegratedConfigurationID(msgHdr);
    IntegratedConfigurationQueryOut queryOut = port.query(queryIn);
    List<MessageHeaderID> lMsgHdr = queryOut.getIntegratedConfigurationID();
    return lMsgHdr;
  }
  
  public static List<IntegratedConfiguration> getICO(String host, String hostport, String username, String upassword, String senderComponent, String senderInterface, String senderInterfaceNamespace) throws Exception {
   
    user = username;
    password = upassword;
    setURL(host, hostport);
    port = getPort();
    MessageHeaderID header = new MessageHeaderID();
    header.setSenderComponentID(senderComponent);
    header.setInterfaceName(senderInterface);
    header.setInterfaceNamespace(senderInterfaceNamespace);
    IntegratedConfigurationReadIn readIn = new IntegratedConfigurationReadIn();
    readIn.getIntegratedConfigurationID().add(header);
    com.sap.xi.basis.IntegratedConfigurationReadOut readOut = port.read(readIn);
    List<IntegratedConfiguration> listico = readOut.getIntegratedConfiguration();
   
    return listico;
  }
  
  public static List<MessageHeaderID> getAllICO(String host, String hostport, String username, String upassword) throws Exception {
    
    user = username;
    password = upassword;
    setURL(host, hostport);
    port = getPort();
    



    IntegratedConfigurationQueryIn queryIn = new IntegratedConfigurationQueryIn();
    
    IntegratedConfigurationQueryOut queryOut = port.query(queryIn);
    List<MessageHeaderID> listico = (ArrayList)queryOut.getIntegratedConfigurationID();
   
    return listico;
  }
  
  private static void setURL(String host, String hostport) {
  
    if ((host == null) || (hostport == null)) {
      return;
    }
    
    String serverPort = host + ":" + hostport;
    url = url.concat("http://").concat(serverPort).concat(apiURL);
    

  }
  
  private static IntegratedConfigurationIn getPort() throws Exception
  {
 
    IntegratedConfigurationIn port = null;
    try {
      com.sap.xi.basis.IntegratedConfigurationInService service = null;
      
      service = new com.sap.xi.basis.IntegratedConfigurationInService();
      
      port = service.getIntegratedConfigurationIn_Port();
      BindingProvider bp = (BindingProvider)port;
      bp.getRequestContext().put("javax.xml.ws.security.auth.username", user);
      bp.getRequestContext().put("javax.xml.ws.security.auth.password", password);
      if (url.length() != 0) {
        bp.getRequestContext().put("javax.xml.ws.service.endpoint.address", url);
        
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  
    return port;
  }
  




  public static void main(String[] args)
  {
    ICOReader test = new ICOReader();
    try {
     // String host = "XX";
     // String hostport = "XX";

      JPanel panel = new JPanel(new BorderLayout(5, 5));

      JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
      label.add(new JLabel("PI System URL", SwingConstants.RIGHT));
      label.add(new JLabel("User ID", SwingConstants.RIGHT));
      label.add(new JLabel("Password", SwingConstants.RIGHT));
      panel.add(label, BorderLayout.WEST);

      //Change Array Loop
    //  label.add(new JLabel("Loop Start", SwingConstants.RIGHT));
    //  label.add(new JLabel("Loop End", SwingConstants.RIGHT));
    //Change Array Loop
      
      
      JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
      JTextField PIUrl = new JTextField();
      controls.add(PIUrl);
      JTextField username = new JTextField();
      controls.add(username);
      JPasswordField passwd = new JPasswordField();
      controls.add(passwd);
    //Change Array Loop
    /*  JTextField loopStart = new JTextField();
      controls.add(loopStart);
      JTextField loopEnd = new JTextField();
      controls.add(loopEnd);*/
    //Change Array Loop
      panel.add(controls, BorderLayout.CENTER);

      JOptionPane.showMessageDialog(null,panel,"login", JOptionPane.QUESTION_MESSAGE);

      String PI_URL = PIUrl.getText();
      String user = username.getText();
      String password = new String(passwd.getPassword());
    //Change Array Loop
     //int lStart = Integer.parseInt(loopStart.getText());
    // int lEnd = Integer.parseInt(loopEnd.getText());
   //Change Array Loop
       String host = PI_URL.split(":")[0];
       String hostport =PI_URL.split(":")[1];
      List<MessageHeaderID> list2 = getAllICO(host, hostport, user, password);
      System.out.println("Total ICO: "+list2.size());
      StringBuffer sb = new StringBuffer("Sender Component,Sender Interface,Sender NS,Receiver Party,Receiver Component,Receiver Channel,Receiver Interface, Receiver Namespace ");
      sb.append("\n");
	  //list2.size() list2.size()-1
     // The size can be specified/reduced in the below loop
      for(int j = 0;j<=Math.min(list2.size()-1,600);j++){
      //for(int j = lStart;j<=lEnd;j++){
    	  MessageHeaderID config1 = (MessageHeaderID)list2.get(j);
    	  String senderComponent = config1.getSenderComponentID();
          String senderInterface = config1.getInterfaceName();
          String senderInterfaceNamespace = config1.getInterfaceNamespace();
     	
        MessageHeaderID header = new MessageHeaderID();
        header.setSenderComponentID(senderComponent);
        header.setInterfaceName(senderInterface);
        header.setInterfaceNamespace(senderInterfaceNamespace);
        IntegratedConfigurationReadIn readIn = new IntegratedConfigurationReadIn();
        readIn.getIntegratedConfigurationID().add(header);
        com.sap.xi.basis.IntegratedConfigurationReadOut readOut = port.read(readIn);
        List<IntegratedConfiguration> listico = readOut.getIntegratedConfiguration();
        for(int i = 0; i<listico.size();i++){
        IntegratedConfiguration config = (IntegratedConfiguration)listico.get(0);
        if (listico.size() <= 0) {
      	  JOptionPane.showMessageDialog(null,"No ICO Found");
        }
        else {
        
      	
      	 sb.append(senderComponent+",");
      	 sb.append(senderInterface+",");
      	 sb.append(senderInterfaceNamespace+",");
      	
      	sb.append(((OutboundProcessing)config.getOutboundProcessing().get(0)).getReceiver().getPartyID()+",");
      	
      	if(config.getOutboundProcessing().size()>1){
      	 for (int i2 = 0; i2 < (config.getOutboundProcessing().size()); i2++) {
      		 
      		 if(i2 != 0){
      			sb.append(",,,,");
      		 }
      		 
     	 sb.append(((OutboundProcessing)config.getOutboundProcessing().get(i2)).getReceiver().getComponentID()+",");
     	sb.append(((OutboundProcessing)config.getOutboundProcessing().get(i2)).getCommunicationChannel().getChannelID()+","); 
     	sb.append(((OutboundProcessing)config.getOutboundProcessing().get(i2)).getReceiverInterface().getName()+","); 
     	sb.append(((OutboundProcessing)config.getOutboundProcessing().get(i2)).getReceiverInterface().getNamespace()+","); 
     	 sb.append("\n");
     	
      	 }
      	}
      	else{
      		 sb.append(((OutboundProcessing)config.getOutboundProcessing().get(0)).getReceiver().getComponentID()+",");
          	sb.append(((OutboundProcessing)config.getOutboundProcessing().get(0)).getCommunicationChannel().getChannelID()+","); 
          	sb.append(((OutboundProcessing)config.getOutboundProcessing().get(0)).getReceiverInterface().getName()+","); 
         	sb.append(((OutboundProcessing)config.getOutboundProcessing().get(0)).getReceiverInterface().getNamespace()+",");
          	sb.append("\n");
      	}
       	 
      	// sb.append("\n");
          
        }
        }
        
     
      }
      
      ExcelCreator excel = new ExcelCreator();
      excel.createExcel(sb.toString());
    //  JTextArea text = new JTextArea(sb.toString());
     // JOptionPane.showMessageDialog(null,text);
      //System.out.println(sb.toString());
    }
    catch (Exception ex)
    {
    	JTextArea text = new JTextArea("Check File path.Exception Received: "+ex.toString());
    	JOptionPane.showMessageDialog(null,text);
      ex.printStackTrace();
    }
  }
}
