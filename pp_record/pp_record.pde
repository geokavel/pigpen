import java.util.Scanner;

int[][] lines;
color[] colors;
PVector board;
Scanner s;
PGraphics fill, stroke;
boolean fast = false;



void setup() {
  size(600, 600);
  frameRate(10);
  File[] frames = sketchFile("frames/").listFiles();
  if(frames != null) {
    for(File f : frames) {
      f.delete();
    }
  }
  lines = new int[][]{new int[]{0, 0, 1, 0}, new int[]{1, 0, 1, 1}, new int[]{0, 1, 1, 1}, new int[]{0, 0, 0, 1}};
  colors = null;
  board = new PVector(width, height);
  selectInput("Select a File (out.txt)","load");
  fill = createGraphics(width, height);
  stroke = createGraphics(width, height);
  noLoop();
}

void load(File f) {
    if(f == null) {
      exit();
    }
    else {
      s = new Scanner(createReader(f));
      loop();
    }
}

void draw() {
  if(s == null) return;
  String pattern  = "[A-Z]:";
  if (s.hasNext(pattern)) {
    background(255);
    fill.beginDraw();
    stroke.beginDraw();
    fill.scale(width/board.x, height/board.y);
    stroke.scale(width/board.x, height/board.y);

    char c = s.next(pattern).charAt(0);
    if (c == 'B') {
      board.set(s.nextInt(), s.nextInt());
      colors = new int[s.nextInt()];
      for (int i = 0; i<colors.length; i++) {
        colors[i] = lerpColor(color(255, 0, 0), color(0, 0, 255), i/(colors.length-1.));
      }
    } else if (c == 'F') {
      int pen = s.nextInt();
      int fence = s.nextInt();
      int player = s.nextInt();
      stroke.stroke(colors[player-1]);
      stroke.translate((pen-1) % int(board.y), (pen-1)/int(board.y));
      int[] l = lines[fence];
      stroke.scale(board.x/width, board.y/height);
      stroke.strokeWeight(10);
      stroke.strokeCap(SQUARE);
      stroke.line(l[0]*width/board.x, l[1]*height/board.y, l[2]*width/board.x, l[3]*height/board.y);
    } else if (c == 'W') {
      int pen = s.nextInt();
      int player = s.nextInt();
      fill.noStroke();
      fill.fill(colors[player-1]);
      fill.translate((pen-1) % int(board.y), (pen-1)/int(board.y));
      fill.rect(0, 0, 1, 1);
    }

    stroke.endDraw();
    fill.endDraw();
    image(fill, 0, 0);
    image(stroke, 0, 0);
    saveFrame("frames/f-###.png");
  } else {
    s.close();
    noLoop();
  }
}

void mouseClicked() {
  fast = !fast;
  frameRate(fast ? 200 : 10);
}