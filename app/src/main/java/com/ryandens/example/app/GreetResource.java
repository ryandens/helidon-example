package com.ryandens.example.app;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

/**
 * A simple JAX-RS resource to greet you. Examples:
 *
 * <p>Get default greeting message: curl -X GET http://localhost:8080/greet
 *
 * <p>Get greeting message for Joe: curl -X GET http://localhost:8080/greet/Joe
 *
 * <p>Change greeting curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Howdy"}'
 * http://localhost:8080/greet/greeting
 *
 * <p>The message is returned as a JSON object.
 */
@Path("/greet")
public class GreetResource {

  /** The greeting message provider. */
  private final GreetingProvider greetingProvider;

  private final GreetingRecorder greetingRecorder;

  /**
   * Using constructor injection to get a configuration property. By default this gets the value
   * from META-INF/microprofile-config
   *
   * @param greetingConfig the configured greeting message
   */
  @Inject
  public GreetResource(GreetingProvider greetingConfig, GreetingRecorder greetingRecorder) {
    this.greetingProvider = greetingConfig;
    this.greetingRecorder = greetingRecorder;
  }

  /**
   * Return a worldly greeting message.
   *
   * @return {@link GreetingMessage}
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public GreetingMessage getDefaultMessage() {
    return createResponse("World");
  }

  /**
   * Return a greeting message using the name that was provided.
   *
   * @param name the name to greet
   * @return {@link GreetingMessage}
   */
  @Path("/{name}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public GreetingMessage getMessage(@PathParam("name") String name) {
    final var response = createResponse(name);
    greetingRecorder.record();
    return response;
  }

  /**
   * Set the greeting to use in future messages.
   *
   * @param message JSON containing the new greeting
   * @return {@link Response}
   */
  @Path("/greeting")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @RequestBody(
      name = "greeting",
      required = true,
      content =
          @Content(
              mediaType = "application/json",
              schema =
                  @Schema(
                      type = SchemaType.OBJECT,
                      requiredProperties = {"greeting"})))
  @APIResponses({
    @APIResponse(name = "normal", responseCode = "204", description = "Greeting updated"),
    @APIResponse(
        name = "missing 'greeting'",
        responseCode = "400",
        description = "JSON did not contain setting for 'greeting'")
  })
  public Response updateGreeting(GreetingMessage message) {
    if (message.getMessage() == null) {
      GreetingMessage entity = new GreetingMessage("No greeting provided");
      return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
    }

    greetingProvider.setMessage(message.getMessage());
    return Response.status(Response.Status.NO_CONTENT).build();
  }

  private GreetingMessage createResponse(String who) {
    String msg = String.format("%s %s!", greetingProvider.getMessage(), who);

    return new GreetingMessage(msg);
  }
}
