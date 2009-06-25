package barbearia;


/**
* barbearia/BarbeiroPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from barbearia.idl
* Quarta-feira, 24 de Junho de 2009 09h34min58s BRT
*/

public abstract class BarbeiroPOA extends org.omg.PortableServer.Servant
 implements barbearia.BarbeiroOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("cortarBarba", new java.lang.Integer (0));
    _methods.put ("cortarCabelo", new java.lang.Integer (1));
    _methods.put ("cortarBigode", new java.lang.Integer (2));
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
       case 0:  // barbearia/Barbeiro/cortarBarba
       {
         short id = in.read_short ();
         boolean $result = false;
         $result = this.cortarBarba (id);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 1:  // barbearia/Barbeiro/cortarCabelo
       {
         short id = in.read_short ();
         boolean $result = false;
         $result = this.cortarCabelo (id);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 2:  // barbearia/Barbeiro/cortarBigode
       {
         short id = in.read_short ();
         boolean $result = false;
         $result = this.cortarBigode (id);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:barbearia/Barbeiro:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Barbeiro _this() 
  {
    return BarbeiroHelper.narrow(
    super._this_object());
  }

  public Barbeiro _this(org.omg.CORBA.ORB orb) 
  {
    return BarbeiroHelper.narrow(
    super._this_object(orb));
  }


} // class BarbeiroPOA
