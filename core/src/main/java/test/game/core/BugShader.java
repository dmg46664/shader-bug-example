package test.game.core;

import static playn.core.PlayN.graphics;

import playn.core.PlayN;
import playn.core.gl.GL20;
import playn.core.gl.GLBuffer.Float;
import playn.core.gl.GLBuffer.Short;
import playn.core.gl.GLContext;
import playn.core.gl.GLShader;

public class BugShader extends GLShader{

	public BugShader(GLContext ctx) {
		super(ctx);
	}
	
	public CanvasShaderCore getCore()
	{
		return (CanvasShaderCore) curCore ;
	}
	
	private final String VERTEX_SHADER_CODE =
			 "attribute vec2 vPosition; //attributes used for connecting to vertex data\n" +
			 "void main(){\n" +
			 "\n" +
			 "\n" +
			"	gl_Position =  vec4(vPosition, 0.0, 1.0); \n" +
			 "}";

	private final String FRAGMENT_SHADER_CODE = 
			"#ifdef GL_ES\n" +
			"precision highp float;\n" +
			"#endif\n" +
			"void main(){              \n" +
			"  gl_FragColor = vec4 (0.7, 0.7, 0.9, 1.0); \n" +
			"}                         \n";
		  

	
	
    protected static final int VERTEX_SIZE = 2; //x,y,r,g,b,a
    protected static final int FLOAT_SIZE_BYTES = 4;
    protected static final int VERTEX_STRIDE = VERTEX_SIZE * FLOAT_SIZE_BYTES;
	
	public class CanvasShaderCore extends GLShader.Core
	{
		private Attrib vPosition;
		private Float vertices;
		private Short elements;
		private int noElements;
		private GL20 gl;

		protected CanvasShaderCore(String vertShader, String fragShader) 
		{
			super(vertShader, fragShader);
			gl = graphics().gl20();
			
			this.checkGlError("BugShader - constructor - program");
						
			//stored in vertices
			vPosition = prog.getAttrib("vPosition", 2, GL20.GL_FLOAT);
			
			this.checkGlError("BugShader - constructor - inputs");
			
			vertices = ctx.createFloatBuffer(0);
			elements = ctx.createShortBuffer(0);
			
			this.checkGlError("BugShader - constructor - buffers");
		}

		@Override
		public void activate(int fbufWidth, int fbufHeight) {
			
			prog.bind();
	        
            vertices.bind(GL20.GL_ARRAY_BUFFER);
            vPosition.bind(VERTEX_STRIDE, 0);

			elements.bind(GL20.GL_ELEMENT_ARRAY_BUFFER);

			this.checkGlError("BugShader - activate");
		}


		@Override
		public void prepare(int tint, boolean justActivated) {
			// TODO Auto-generated method stub

		}

		@Override
		public void flush() {
			drawLines();
		}
		
		@Override
        public void destroy () 
		{
            super.destroy();
            vertices.destroy();
            elements.destroy();
        }

		@Override
		public void addQuad(float m00, float m01, float m10, float m11,
				float tx, float ty, float x1, float y1, float sx1, float sy1,
				float x2, float y2, float sx2, float sy2, float x3, float y3,
				float sx3, float sy3, float x4, float y4, float sx4, float sy4) {
			// TODO Auto-generated method stub

		}


		public void drawLines()
		{
			//hard coded elements for demonstration
			noElements = 4;
			
			vertices.expand(noElements * VERTEX_SIZE ) ;
			elements.expand(noElements);

			//element index id
			int vertIdx = 0 ;

			float left = -1f;
			float right = 1f;
			float top = 1f;
			float bottom = -1f;

			//line
			vertices.add(left) ;
			vertices.add(bottom) ;
			elements.add(vertIdx++);

			vertices.add(right) ;
			vertices.add(top) ;
			elements.add(vertIdx++);	
			
			//line
			vertices.add(left) ;
			vertices.add(top) ;
			elements.add(vertIdx++);

			vertices.add(right) ;
			vertices.add(bottom) ;
			elements.add(vertIdx++);
			
			gl.glLineWidth(0.5f);

			if (vertices.position() != 0)
			{
				vertices.send(GL20.GL_ARRAY_BUFFER, GL20.GL_STREAM_DRAW); //2nd argument is a hint
				elements.send(GL20.GL_ELEMENT_ARRAY_BUFFER, GL20.GL_STREAM_DRAW);
			}
			elements.drawElements(GL20.GL_LINES, noElements);
			this.checkGlError("CanvasShader - GL_LINES");

		}

		private void checkGlError(String msg) {
			int err = gl.glGetError();
			if (err != GL20.GL_NO_ERROR) {
				PlayN.log().info("GL Error: " + err + " @ " + msg);
			}
		}
		
	} //end of CanvasShaderCore

	@Override
	protected Core createTextureCore() {
		return null; //no texture core.
	}

	@Override
	protected Core createColorCore() {
		return new CanvasShaderCore(this.VERTEX_SHADER_CODE, this.FRAGMENT_SHADER_CODE) ;
	}
}