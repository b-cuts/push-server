package com.cosmicpush.plugins.ocs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.microsoft.security.ntlm.NtlmAuthenticator;
import org.microsoft.security.ntlm.NtlmSession;
import org.microsoft.security.ntlm.impl.Algorithms;
import org.testng.annotations.Test;

import javax.sip.*;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.*;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Test(enabled = false)
public class NtlmTest {

  private static final Log log = LogFactory.getLog(NtlmTest.class);

  private static final String DEFAULT_SIP_EPID = "d3665ff3bd3e";
  private static final String SIP_TAG = "12345";
  private static final String TRANSPORT_STRING = "TLS";

  private TestSipListener sipListener = new TestSipListener();
  private long sequenceNumber = 1;

  public void ntlmAuthenticationTest() throws Exception {

    int sipAuthVersion = 4;
    String sipServer = "oakwinocs.stcg.net";
    String sipHostname = "isrobkmac2.stcg.net";
    String sipDomain = "stcg.net";

    String fromUser = "robertk";

    String password = System.getProperty("password");

    NtlmAuthenticator ntlmAuthentication = new NtlmAuthenticator(NtlmAuthenticator.NtlmVersion.ntlmv2,
            NtlmAuthenticator.ConnectionType.connectionless
            , "oakwinocs", "stcg.net", fromUser, password);

    NtlmSession ntlmSession = ntlmAuthentication.createSession();

    SipFactory sipFactory = SipFactory.getInstance();
    sipFactory.setPathName("gov.nist");

    Properties properties = new Properties();
    properties.setProperty("javax.sip.OUTBOUND_PROXY", sipServer + "/" + TRANSPORT_STRING);
    properties.setProperty("javax.sip.STACK_NAME", "shootistAuth");
    properties.setProperty("gov.nist.javax.sip.MAX_MESSAGE_SIZE", "1048576");
    properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "OfficeCommunicatorAuthdebug.txt");
    properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "OfficeCommunicatorAuthlog.txt");
    properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "LOG4J");

    // Drop the client connection after we are done with the transaction.
    properties.setProperty("gov.nist.javax.sip.CACHE_CLIENT_CONNECTIONS", "false");
    properties.setProperty("gov.nist.javax.sip.NETWORK_LAYER", TestSslNetworkLayer.class.getName());

    // Create SipStack object
    SipStack sipStack = sipFactory.createSipStack(properties);
    log.trace("createSipStack " + sipStack);

    int port = TRANSPORT_STRING.equalsIgnoreCase("tcp") ? 5060 : 5061;

    ListeningPoint listeningPoint = sipStack.createListeningPoint(sipHostname, port, TRANSPORT_STRING);
    SipProvider sipProvider = sipStack.createSipProvider(listeningPoint);
    sipProvider.addSipListener(sipListener);
    sipListener.sendRequest(sipFactory, sipProvider, sipHostname, sipDomain, fromUser, fromUser);
  }

  private String calculateSipUuid(String epid) {
    byte[] SIP_UUID_NAMESPACE = Algorithms.stringToBytes("03 fb ac fc 73 8a ef 46 91 b1 e5 eb ee ab a4 fe");
    byte[] bytes = Algorithms.concat(SIP_UUID_NAMESPACE, epid.getBytes(Algorithms.ASCII_ENCODING));
    byte[] sha1 = Algorithms.calculateSHA1(bytes);
    char[] out = new char[16*2+4];
    Algorithms.bytesToCharsReverse(sha1, 0, 4, out, 0); // time_low
    out[8] = '-';
    Algorithms.bytesToCharsReverse(sha1, 4, 2, out, 9); // time_mid
    out[13] = '-';

    /*
      8. Set the four most significant bits, which are bits 12 through 15, of the time_hi_and_version
      field to the 4-bit version number, as specified in [RFC4122] section 4.1.3. For name-based UUIDs
      computed with the SHA-1 function, this sequence is 0101.
     */
    sha1[7] = (byte) (sha1[7] & 0xf | 0x50);
    Algorithms.bytesToCharsReverse(sha1, 6, 2, out, 14); // time_hi_and_version
    out[18] = '-';

    /*
      10.Set the two most significant bits, which are bits 6 and 7, of the clock_seq_hi_and_reserved to
      zero and 1, respectively.
     */
    sha1[8] = (byte) (sha1[8] & 0x3f | 0x80);
    Algorithms.bytesToChars(sha1, 8, 2, out, 19); // clock_seq_hi_and_reserved

    out[23] = '-';
    Algorithms.bytesToChars(sha1, 10, 6, out, 24);
    return new String(out);
  }



  class TestSipListener implements SipListener {

    Log log = LogFactory.getLog(TestSipListener.class);
    private Response response;

    public TestSipListener() {}

    @Override
    public void processRequest(RequestEvent requestEvent) {
      Request request = requestEvent.getRequest();
      ServerTransaction serverTransactionId = requestEvent.getServerTransaction();

      log.trace(" >>> Request " + request.getMethod()
              + " with server transaction id " + serverTransactionId);

      // We are the UAC so the only request we get is the BYE.
      if (request.getMethod().equals(Request.BYE))
        log.trace("shootist:  got a bye .");
    }

    @Override
    public void processResponse(ResponseEvent responseEvent) {
      response = responseEvent.getResponse();
      System.out.println(response.getStatusCode());
      ClientTransaction tid = responseEvent.getClientTransaction();
      CSeqHeader cseq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);

      log.trace("Response received : Status Code = " + response.getStatusCode() + " " + cseq);
      if (tid == null) {
        log.trace("Stray response -- dropping ");
        return;
      }
      log.trace("transaction state is " + tid.getState());
      Dialog dialog = tid.getDialog();
      log.trace("Transaction = " + tid + ", Dialog = " + dialog + (dialog == null ? "--" : tid.getDialog().getState()));
    }

    @Override
    public void processTimeout(TimeoutEvent timeoutEvent) {
      log.trace("Transaction Time out");
    }

    @Override
    public void processIOException(IOExceptionEvent ioExceptionEvent) {
      log.trace("IOException happened for "
              + ioExceptionEvent.getHost() + " port = "
              + ioExceptionEvent.getPort());
    }

    @Override
    public void processTransactionTerminated(TransactionTerminatedEvent transactionTerminatedEvent) {
      log.trace("Transaction terminated event received");
    }

    @Override
    public void processDialogTerminated(DialogTerminatedEvent dialogTerminatedEvent) {
      log.trace("dialogTerminatedEvent");
    }

    public Response getResponse() {
      return response;
    }

    private Response sendRequest(SipFactory sipFactory, SipProvider sipProvider, String sipHostname, String sipDomain, String fromUsername, String toUsername)
            throws SipException, ParseException, InvalidArgumentException, InterruptedException, TimeoutException, BrokenBarrierException {

      HeaderFactory headerFactory = sipFactory.createHeaderFactory();
      AddressFactory addressFactory = sipFactory.createAddressFactory();
      MessageFactory messageFactory = sipFactory.createMessageFactory();

      // create >From Header
      SipURI fromAddress = addressFactory.createSipURI(fromUsername, sipDomain);
      Address fromNameAddress = addressFactory.createAddress(fromAddress);
      FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress, SIP_TAG);












    /*WWWAuthenticateHeader ntlmAuthenticateHeader = (WWWAuthenticateHeader) response.getHeader(WWWAuthenticateHeader.NAME);
    WWWAuthenticateHeader ntlmAuthenticateHeader = (WWWAuthenticateHeader) response.getHeader(WWWAuthenticateHeader.NAME);
    userInfo.opaque = ntlmAuthenticateHeader.getOpaque();
    userInfo.cnum = 1;
    final String gssapiDataString = ntlmAuthenticateHeader.getParameter("gssapi-data");
    final byte[] gssapiData = Algorithms.decodeBase64(gssapiDataString);

    realm = ntlmAuthenticateHeader.getRealm();
    targetname = ntlmAuthenticateHeader.getParameter("targetname");

    ntlmSession.processChallengeMessage(gssapiData);
    String newGssapiData = Algorithms.encodeBase64(ntlmSession.generateAuthenticateMessage());

    //
    // Event
    //
    userInfo.createNextNewRequest();

    AuthorizationHeader authorizationHeader = headerFactory.createAuthorizationHeader("NTLM");

    authorizationHeader.setParameter("gssapi-data", '"' + newGssapiData + '"');
    authorizationHeader.setParameter("version", "" + SipAuthVersion);
    Response newResponse = userInfo.sendNtlmRequest(authorizationHeader);*/












      // epid is defined in [MS-SIPRE]
      fromHeader.setParameter("epid", DEFAULT_SIP_EPID);

      // create To Header
      SipURI toAddress = addressFactory.createSipURI(toUsername, sipDomain);
      Address toNameAddress = addressFactory.createAddress(toAddress);
      ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);

      // Create ViaHeaders
      ArrayList<ViaHeader> viaHeaders = new ArrayList<ViaHeader>();
      ViaHeader viaHeader = headerFactory.createViaHeader(sipHostname, sipProvider.getListeningPoint(TRANSPORT_STRING).getPort(), TRANSPORT_STRING, null);
      // add via headers
      viaHeaders.add(viaHeader);

      // create Request URI
      URI requestURI = addressFactory.createURI("sip:" + sipDomain);

      // Create a new CallId header
      CallIdHeader callIdHeader = sipProvider.getNewCallId();

      // Create a new Cseq header
      String method = "REGISTER";
      CSeqHeader cSeqHeader = headerFactory.createCSeqHeader(sequenceNumber++, method);

      // Create a new MaxForwardsHeader
      MaxForwardsHeader maxForwards = headerFactory.createMaxForwardsHeader(70);

      // Create the request.
      Request request = messageFactory.createRequest(requestURI,
              method, callIdHeader, cSeqHeader, fromHeader, toHeader,
              viaHeaders, maxForwards);


      SipURI contactUrl = addressFactory.createSipURI(fromUsername, sipDomain);

      Address contactAddress = addressFactory.createAddress(contactUrl);
      ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);
      contactHeader.setParameter("proxy", "replace");
      contactHeader.setParameter("+sip.instance", "\"<urn:uuid:" + calculateSipUuid(DEFAULT_SIP_EPID) + ">\"");

      request.addHeader(contactHeader);


      // Create the client transaction.
      ClientTransaction transaction = sipProvider.getNewClientTransaction(request);
      transaction.setApplicationData(this);
      Dialog dialog = transaction.getDialog();

      CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

      // send the request out.
      transaction.sendRequest();

      cyclicBarrier.await(10, TimeUnit.SECONDS);

      return response;
    }
  }

}
