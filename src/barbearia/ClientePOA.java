package barbearia;


/**
* barbearia/ClientePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from barbearia.idl
* Terça-feira, 9 de Junho de 2009 09h12min53s BRT
*/

public abstract class ClientePOA extends org.omg.PortableServer.Servant
 implements barbearia.ClienteOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("Concorrer", new java.lang.Integer (0));
    _methods.put ("msgOK", new java.lang.Integer (1));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // barbearia/Cliente/Concorrer
       {
         barbearia.Mensagem msg = barbearia.MensagemHelper.read (in);
         this.Concorrer (msg);
         out = $rh.createReply();
         break;
       }

       case 1:  // barbearia/Cliente/msgOK
       {
         short id = in.read_short ();
         this.msgOK (id);
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:barbearia/Cliente:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Cliente _this() 
  {
    return ClienteHelper.narrow(
    super._this_object());
  }

  public Cliente _this(org.omg.CORBA.ORB orb) 
  {
    return ClienteHelper.narrow(
    super._this_object(orb));
  }


} // class ClientePOA
