package webserver.http.response;

public class Response {

    private final byte[] header;
    private final byte[] body;

    public Response(byte[] header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public byte[] getResponseMessage() {
        byte[] responseMessage = new byte[header.length + body.length];

        System.arraycopy(header, 0, responseMessage, 0, header.length);
        System.arraycopy(body, 0, responseMessage, header.length, body.length);

        return responseMessage;
    }
}
