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
			 "uniform vec2 uScreenSize;\n" + 
			 "uniform vec2 uOffset;\n" + 
			 "uniform float uScale;\n" + 
			 "attribute vec2 vPosition; //attributes used for connecting to vertex data\n" +
			 "attribute vec4 aColor;\n" + 
			 "varying vec4 v_Color;\n" + 
			 "void main(){\n" + 
			 "	mat4 toScreenM = mat4(uScale, 0, 0, 0,\n" + 
			 "							0, uScale, 0, 0,\n" + 
			 "							0, 0, 0, 0,\n" + 
			 "							uOffset.x, uOffset.y, 0, 1) ;\n" + 
			 "\n" + 
			 "	mat4 toNormalM = mat4(2.0/uScreenSize.x, 0, 0, 0,\n" + 
			 "							0, -2.0/uScreenSize.y, 0, 0,\n" + 
			 "							0, 0, 0, 0,\n" + 
			 "							-1.0, 1.0, 0, 1) ;\n" + 
			 "\n" + 
			 "	gl_Position =  toNormalM * toScreenM * vec4(vPosition, 0.0, 1.0); \n" +
			 "	v_Color = aColor ;\n" + 
			 "}";

	private final String FRAGMENT_SHADER_CODE = 
			"#ifdef GL_ES\n" +
			"precision highp float;\n" +
			"#endif\n" +
			"varying vec4 v_Color; \n" +
			"void main(){              \n" +
			"  gl_FragColor = v_Color ; \n" +
//			"  gl_FragColor = vec4 (0.7, 0.7, 0.9, 1.0); \n" +
			"}                         \n";
		  

	
	
    protected static final int VERTEX_SIZE = 6; //x,y,r,g,b,a
    protected static final int FLOAT_SIZE_BYTES = 4;
    protected static final int VERTEX_STRIDE = VERTEX_SIZE * FLOAT_SIZE_BYTES;
	
	public class CanvasShaderCore extends GLShader.Core
	{

		public int lastActionSizeCount = 0 ;
		private Attrib vPosition;
		private Attrib aColour;
		private Float vertices;
		private Short elements;
		private Uniform1f uScale;
		private Uniform2f uOffset;
		private Uniform2f uScreenSize;
		private int noElements;
		private GL20 gl;

		protected CanvasShaderCore(String vertShader, String fragShader) 
		{
			super(vertShader, fragShader);
			gl = graphics().gl20();
			
			this.checkGlError("BugShader - constructor - program");

			//stored in projection			
			uScreenSize = prog.getUniform2f("uScreenSize");
			uOffset = prog.getUniform2f("uOffset");
			uScale = prog.getUniform1f("uScale");			
						
			//stored in vertices
			aColour = prog.getAttrib("aColor", 4, GL20.GL_FLOAT);
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
            aColour.bind(VERTEX_STRIDE, 8);
            

            //hard coded values for demonstration
            uScreenSize.bind((float) graphics().width(), (float) graphics().height()); //only once, so no stride
			uOffset.bind(0f,  0f) ;
			uScale.bind(1f);

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

			//line
			vertices.add(200f) ;
			vertices.add(200f) ;
			vertices.add(0.7f).add(0.7f).add(0.9f).add(1.0f);
			elements.add(vertIdx++);

			vertices.add(800f) ;
			vertices.add(800f) ;
			vertices.add(0.7f).add(0.7f).add(0.9f).add(1.0f);						
			elements.add(vertIdx++);	
			
			//line
			vertices.add(200f) ;
			vertices.add(800f) ;
			vertices.add(0.7f).add(0.7f).add(0.9f).add(1.0f);
			elements.add(vertIdx++);

			vertices.add(800f) ;
			vertices.add(200f) ;
			vertices.add(0.7f).add(0.7f).add(0.9f).add(1.0f);						
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
