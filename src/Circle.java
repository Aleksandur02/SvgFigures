public class Circle {
        private int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        private float cx;
        private float cy;
        private float r;
        private String color;

        @Override
        public String toString() {
            return  index + "." +
                    " circle"+
                    " cx=" + cx +
                    ", cy=" + cy +
                    ", r=" + r +
                    ", color='" + color + '\''
                    ;
        }

        public Circle(float cx, float cy, float r, String color) {
            this.cx = cx;
            this.cy = cy;
            this.r = r;
            this.color = color;
        }

        public float getCx() {
            return cx;
        }

        public void setCx(float cx) {
            this.cx = cx;
        }

        public float getCy() {
            return cy;
        }

        public void setCy(float cy) {
            this.cy = cy;
        }

        public float getR() {
            return r;
        }

        public void setR(float r) {
            this.r = r;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }


