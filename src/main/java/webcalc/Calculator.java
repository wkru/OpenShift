package webcalc;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

import bsh.Interpreter;

class Expression {
	private String expression;

	public String getExpression() {
		return expression;
	}
}

class Result {
	private String result;
	private boolean error;

	public void setResult(String result) {
		this.result = result;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}

@Path("/calculate")
public class Calculator {

	Interpreter shell = new Interpreter();
	Gson gson = new Gson();
	Expression expression = new Expression();
	Result result;

	@GET
	@Produces(javax.ws.rs.core.MediaType.TEXT_HTML)
	public String GETHTMLFallback() {
		return "<html><head><title>Webcalc</title></head><body><h1>Webcalc</h1>Use the POST HTTP method to calculate expressions.</body></html>";
	}

	@POST
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	public String calculate(String inputJson) {
		expression = gson.fromJson(inputJson, Expression.class);
		result = new Result();

		try {
			shell.eval("double result = " + expression.getExpression());

			result.setResult(((Double) shell.get("result")).toString());
			result.setError(false);
		} catch (Exception e) {
			result.setResult("Błąd");
			result.setError(true);
		}

		return gson.toJson(result);
	}
}
