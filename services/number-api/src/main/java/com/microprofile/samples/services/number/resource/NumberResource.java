package com.microprofile.samples.services.number.resource;

import com.microprofile.samples.services.number.config.GenerationPrefix;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@ApplicationScoped
@Path("numbers")
@Produces(MediaType.TEXT_PLAIN)
@Traced
@Tag(name = "ISBN resource", description = "Generate an ISBN number for a book.")
public class NumberResource {
    private final Logger logger = Logger.getLogger(NumberResource.class.getName());

    @Inject
    @ConfigProperty(name = "GENERATION_PREFIX", defaultValue = "UN")
    private GenerationPrefix prefix;

    @GET
    @Path("/generate")
    @Metered(description = "Metrics for ISBN random generation")
    @Timed(description = "Metrics to monitor the times of generate ISBN method.",
           unit = MetricUnits.MILLISECONDS,
           absolute = true)
    @Operation(description = "Generate an ISBN for a book.")
    @APIResponses({
                      @APIResponse(
                          responseCode = "200",
                          description = "Successful, returning the value",
                          content = @Content(
                              schema = @Schema(
                                  implementation = String.class
                              )
                          ))
                  })
    public Response generate() {
        return Response.ok(number()).build();
    }

    private String number() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        return prefix.toString() + "-" + (int) Math.floor((Math.random() * 9999999)) + 1;
    }
}
