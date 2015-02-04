package com.cosmicpush.plugins.ocs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sip.*;
import javax.sip.header.CSeqHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

class TestSipListenerOuter implements SipListener {

  Log log = LogFactory.getLog(TestSipListenerOuter.class);
  private Response response;

  public TestSipListenerOuter() {}

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
}