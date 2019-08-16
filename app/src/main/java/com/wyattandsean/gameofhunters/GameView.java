package com.wyattandsean.gameofhunters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class GameView extends View
{
    private static final float TEXT_SIZE = 60;
    final long UPDATE_MILLISECONDS = 120;
    ArrayList<Ghost> ghosts;
    ArrayList<Bat> bats;
    ArrayList<Explosion> explosions;
    // The player's fireballs
    private ArrayList<Fireball> fireballs;
    private Context context;
    private int batScore = 0;
    private int ghostScore = 0;
    private static int level = 1;
    private int playerHealth = 10;
    private GhostHuntButton exitButton;
    private int shootSound = 0;
    private int scoreSound = 0;
    private int totalScore = 0;
    private String timerString = "0";
    private Paint scorePaint, healthPaint;

    Arrow leftArrow;
    Arrow rightArrow;
    Bitmap background;
    Hunter hunter;
    Handler handler;
    public static int displayWidth;
    public static int displayHeight;

    /**
     * background image will be stretched
     * rectangle corner positions:
     * top left: 0, 0
     * right bottom: displayWidth, displayHeight
     */
    Rect rectangle;

    Runnable runnable;
    SoundPool soundPool;
    private Paint timerPaint;
    private TextView timerTextView;

    public GameView(Context context)
    {
        super(context);

        this.context = context;

        stretchBackgroundImage();

        createGameObjects();

        createSoundObjects();

        configureCountDownTimer();

        /**
         * A Handler allows you to send and process Message and
         * Runnable objects associated with a thread's MessageQueue.
         * Each Handler instance is associated with a single thread and
         * that thread's message queue. When you create a new Handler,
         * it is bound to the thread / message queue of the thread that
         * is creating it -- from that point on, it will deliver
         * messages and runnables to that message queue and execute
         * them as they come out of the message queue.
         */

        handler = new Handler();

        rectangle = new Rect(0, 0, displayWidth, displayHeight);

        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                // this forces a re-draw and view re-drawn
                invalidate();
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // this is where the player put his finger
        float touchX = event.getX();
        float touchY = event.getY();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN)
        {
//            Log.i("onTouchEvent", "is tapped downward.  touchX: " + touchX + ". touchY: " +
//                    touchY + ". hunter x between " + (displayWidth / 2 - hunter.hunterWidth / 2) +
//                    " and " + (displayWidth / 2 + hunter.hunterWidth / 2) + ".  hunter height: " +
//                    (displayHeight - 350));

            Log.i("onTouchEvent", "is tapped downward.  touchX: " + touchX + ". touchY: " +
                    touchY + ". left arrow between " + (displayWidth / 2 - leftArrow.getWidth() / 2) +
                    " and " + (displayWidth / 2 + leftArrow.getWidth() / 2) + ".  leftArrow height: " +
                    leftArrow.getHeight());

            Log.i("onTouchEvent", "is tapped downward.  touchX: " + touchX + ". touchY: " +
                    touchY + ". hunter x between " + (displayWidth / 2 - hunter.hunterWidth / 2) +
                    " and " + (displayWidth / 2 + hunter.hunterWidth / 2) + ".  hunter height: " +
                    (displayHeight - 350));

            // tapping the hunter to SHOOT
//            todo: remove hard code***
            boolean touchingHunterLeftSide = touchX >= (hunter.hunterX - (hunter.hunterWidth / 2));

            boolean touchingHunterRightSide = touchX <= (hunter.hunterX + (hunter.hunterWidth / 2));

            Log.d("exitButton X", "exitButton X:" + exitButton.buttonX);

            Log.d("exitButton Y", "exitButton Y:" + exitButton.buttonY);

            int exitButtonLeftSideX = exitButton.buttonX - 25;

            int exitButtonRightSideX = exitButton.buttonX + 25;

            int exitButtonTopSideY = exitButton.buttonY + 50;

            int exitButtonBottomSideY = exitButton.buttonY - 50;

            boolean touchingExitButtonLeftSide = touchX >= exitButtonLeftSideX;

            boolean touchingExitButtonRightSide = touchX <= exitButtonRightSideX;

            boolean touchingExitButtonTopSide = touchY <= exitButtonTopSideY;

            boolean touchingExitButtonBottomSide = touchY >= exitButtonBottomSideY;

            Log.d("touchingExitButtonLeftSide", "touchingExitButtonLeftSide is " + touchingExitButtonLeftSide);
            Log.d("touchingExitButtonRightSide", "touchingExitButtonRightSide is " + touchingExitButtonRightSide);
            Log.d("touchingExitButtonTopSide", "touchingExitButtonTopSide is " + touchingExitButtonTopSide);
            Log.d("touchingExitButtonBottomSide", "touchingExitButtonBottomSide is " + touchingExitButtonBottomSide);


            if (touchingHunterRightSide &&
                    touchingHunterLeftSide &&
                    touchY <= (displayHeight - 150))
            {
                Log.i("Hunter", "is tapped downward.  touchX: " + touchX + ". touchY: " +
                        touchY);
                // create more fireballs
                // todo: create ammo loot to pick up**
                // todo: add a pase button*?
                if (this.fireballs.size() < 3)
                {
                    int gunBarrelX = this.hunter.hunterX + 100;

                    Fireball fireball = new Fireball(context, gunBarrelX);

                    fireballs.add(fireball);

                    if (shootSound != 0)
                    {
                        this.soundPool.play(shootSound, 1, 1, 0, 0,
                                1);
                    }
                }
            }
            else if (touchX >= (exitButtonLeftSideX / 4 - leftArrow.getWidth() / 2) &&
                    touchX <= (displayWidth / 4 + leftArrow.getWidth() / 2) &&
                    touchY >= (displayHeight - leftArrow.getHeight()))
            {

                Log.i("Hunter", "is tapped left before.  touchX: " + touchX + ". touchY: " +
                        touchY + ".  hunter x: " + hunter.hunterX);

                this.hunter.hunterX -= 20;

                Log.i("Hunter", "is tapped left after.  touchX: " + touchX + ". touchY: " +
                        touchY + ".  hunter x: " + hunter.hunterX);
            }
            else if (touchX >= (displayWidth / 4 * 3 - rightArrow.getWidth() / 2) &&
                    touchX <= (displayWidth / 4 * 3 + rightArrow.getWidth() / 2) &&
                    touchY >= (displayHeight - rightArrow.getHeight()))
            {
                Log.i("Hunter", "is tapped right before. touchX: " + touchX + ". touchY: " +
                        touchY);

                this.hunter.hunterX += 20;

                Log.i("Hunter", "is tapped right. touchX: " + touchX + ". touchY: " +
                        touchY);
            }
            else if(touchingExitButtonLeftSide == true && touchingExitButtonRightSide == true
            && touchingExitButtonBottomSide == true && touchingExitButtonTopSide == true)
            {
                Intent intent = new Intent(this.context, MainActivity.class);

                this.context.startActivity(intent);
            }
        }



        // returning true: because we are handling onTouch event above
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
//        Log.i("GameView - onDraw()", "starting onDraw() ");

        super.onDraw(canvas);

        // use this until we implement the rectangle
//        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(background, null, rectangle, null);

        checkLevel(canvas);

        drawObjects(canvas);

        // re-draw images for the canvas
        // schedule runnable to be executed in future
        handler.postDelayed(runnable, UPDATE_MILLISECONDS);
    }

    private void checkLevel(Canvas canvas)
    {
        if (totalScore > 10 && totalScore < 21)
        {
            level = 2;
        }
        else if (totalScore >= 21)
        {
            level = 3;
        }

        if (level == 1)
        {
            canvas.drawBitmap(background, null, rectangle, null);
        }
        else if (level == 2)
        {
//            canvas.drawBitmap(background2, null, rect, null);
        }
        else if (level == 3)
        {
//            canvas.drawBitmap(background3, null, rect, null);
        }

        // change speed of ghosts
//        if (GameView.level == 1) {
//            velocity = 8 + random.nextInt(13);
//        } else if (GameView.level == 2) {
//            velocity = 13 + random.nextInt(13);
//        } else if (GameView.level == 3) {
//            velocity = 18 + random.nextInt(13);
//        }

        // random speed of second mob
//        if (GameView.level == 1) {
//            velocity = 5 + random.nextInt(21);
//        } else if (GameView.level == 2) {
//            velocity = 10 + random.nextInt(21);
//        } else if (GameView.level == 3) {
//            velocity = 15 + random.nextInt(13);
//        }
    }

    private void checkPlayerHealth()
    {
        // check if player is dead
        if (playerHealth == 0)
        {
            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("score", this.totalScore);

            context.startActivity(intent);

            ((Activity) context).finish();
        }
    }

    private void createSoundObjects()
    {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        this.soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();

        this.shootSound = soundPool.load(this.context, R.raw.fire, 1);
        this.scoreSound = soundPool.load(this.context, R.raw.point, 1);
    }

    private void createGameObjects()
    {
        bats = new ArrayList<Bat>();
        exitButton = new GhostHuntButton(this.context);
        explosions = new ArrayList<Explosion>();
        fireballs = new ArrayList<Fireball>();
        ghosts = new ArrayList<Ghost>();
        hunter = new Hunter(displayWidth, this.context);
//      add player movement
        leftArrow = new Arrow(this.context, false);
        rightArrow = new Arrow(this.context, true);

        // create collection of ghosts and bats
        for (int ghostCounter = 0; ghostCounter < 3; ghostCounter++)
        {
            Ghost ghost = new Ghost(this.context);
            ghosts.add(ghost);

            Bat bat = new Bat(this.context);
            bats.add(bat);
        }

        configureScore();

//            todo: work on health functionality**
        configureHealth();
    }

    //TODO: add back to start button*
    private void configureCountDownTimer(){
        this.timerPaint = new Paint();
        this.timerPaint.setTextSize(TEXT_SIZE);
        this.timerPaint.setTextAlign(Paint.Align.LEFT);
        this.timerPaint.setColor(Color.argb(255, 255, 185, 0));

        this.timerTextView = new TextView(this.context);

        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)

            {
                String timerString = String.valueOf(millisUntilFinished / 1000);

                timerTextView.setText(timerString);

                Log.d("gameTimer", timerString);
            }

            @Override
            public void onFinish()
            {
                timerTextView.setText("0");
            }
        };

        countDownTimer.start();
    }

    private void configureScore()
    {
        this.batScore = 0;
        this.ghostScore = 0;
        this.scorePaint = new Paint();
        this.scorePaint.setTextSize(TEXT_SIZE);
        this.scorePaint.setTextAlign(Paint.Align.LEFT);
        this.scorePaint.setColor(Color.argb(255, 255, 185, 0));
    }

    private void configureHealth()
    {
        this.healthPaint = new Paint();
        this.healthPaint.setColor(Color.GREEN);
    }

    /**
     * The background image is smaller than the screen
     * Need to fill it out
     */
    private void stretchBackgroundImage()
    {
        background = BitmapFactory.decodeResource(getResources(), R.drawable.forest);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;

        Log.i("stretchBackgroundImage()", "display height: " + displayHeight +
                "display Width: " + displayWidth);
    }

    /**
     * Upon onDraw event, we'll update objects on screen for user inputs
     *
     * @param canvas
     */
    private void drawObjects(Canvas canvas)
    {
        // todo: when bombs drop on player**
        checkPlayerHealth();

        drawFireballs(canvas);

        drawExplosions(canvas);

        drawDirectionArrows(canvas);

        drawHunter(canvas);

        drawGhosts(canvas);

        drawBats(canvas);

        drawScore(canvas);

        //drawHealth(canvas);

        drawExitButton(canvas);

        drawCountDownTimer(canvas);
    }

    private void drawFireballs(Canvas canvas)
    {
        // Initialize the fireballs array
        for (int fireballCounter = 0; fireballCounter < fireballs.size(); fireballCounter++)
        {
            if (fireballs.get(fireballCounter).fireballY > -fireballs.get(fireballCounter)
                    .getFireballHeight())
            {
                fireballs.get(fireballCounter).fireballY -= fireballs.get(fireballCounter)
                        .fireballVector;

                // fireball shot here
                canvas.drawBitmap(fireballs.get(fireballCounter).fireballImage,
                        fireballs.get(fireballCounter).fireballX, fireballs.get(fireballCounter)
                                .fireballY, null);

                if (isGhostCollision(fireballCounter, 0) == true)
                {
                    // do explosion
                    doExplosionForGhosts(context, ghosts, explosions, 0);

                    // if fireball hits ghost, reset position and update score
                    ghosts.get(0).resetPosition();

                    ghostScore++;

                    fireballs.remove(fireballCounter);

                    this.playScoreSound();
                }
                else if (isGhostCollision(fireballCounter, 1) == true)
                {
                    // do explosion
                    doExplosionForGhosts(context, ghosts, explosions, 1);

                    // if fireball hits ghost, reset position and update score
                    ghosts.get(1).resetPosition();

                    ghostScore++;

                    fireballs.remove(fireballCounter);

                    this.playScoreSound();
                }
                else if (isGhostCollision(fireballCounter, 2) == true)
                {
                    // do explosion
                    doExplosionForGhosts(context, ghosts, explosions, 2);

                    // if fireball hits ghost, reset position and update score
                    ghosts.get(2).resetPosition();

                    ghostScore++;

                    fireballs.remove(fireballCounter);

                    this.playScoreSound();
                }
                else if (isBatCollision(fireballCounter, 0) == true)
                {
                    // do explosion
                    doExplosionForBats(0, context, bats, explosions);

                    // if fireball hits bat, reset position and update score
                    bats.get(0).resetPosition();

                    batScore++;

                    fireballs.remove(fireballCounter);

                    this.playScoreSound();
                }
                else if (isBatCollision(fireballCounter, 1) == true)
                {
                    // do explosion
                    doExplosionForBats(1, context, bats, explosions);

                    // if fireball hits bat, reset position and update score
                    bats.get(1).resetPosition();

                    batScore++;

                    fireballs.remove(fireballCounter);

                    this.playScoreSound();
                }
                else if (isBatCollision(fireballCounter, 2) == true)
                {
                    // do explosion
                    doExplosionForBats(2, context, bats, explosions);

                    // if fireball hits bat, reset position and update score
                    bats.get(2).resetPosition();

                    batScore++;

                    fireballs.remove(fireballCounter);

                    this.playScoreSound();
                }
            }
            else
            {
                fireballs.remove(fireballCounter);
            }
        }
    }

    /**
     * Create a hit box by comparing position of two objects in relation
     * to each other
     * <p>
     * start with the fireball x and add 1/2 the width of the fireball to
     * see if it touches the (bat x and 1/2 of the bat width)
     *
     * @param fireballCounter
     * @param mobIndex
     * @return
     */
    private boolean isBatCollision(int fireballCounter, int mobIndex)
    {
        boolean touchingFromLeftSide = false;
        boolean touchingFromRightSide = false;
        boolean touchingFromTopSide = false;
        boolean touchingFromBottomSide = false;

        touchingFromLeftSide = fireballs.get(fireballCounter).fireballX +
                (fireballs.get(fireballCounter).getFireballWidth() / 2) >= bats.get(mobIndex).batX -
                (bats.get(mobIndex).getWidth() / 2);

        touchingFromRightSide = (fireballs.get(fireballCounter).fireballX -
                fireballs.get(fireballCounter).getFireballWidth() / 2) <= (bats.get(mobIndex).batX +
                bats.get(mobIndex).getWidth() / 2);

        touchingFromBottomSide = fireballs.get(fireballCounter).fireballY -
                (fireballs.get(fireballCounter).getFireballHeight() / 2) <= bats.get(mobIndex).batY +
                (bats.get(mobIndex).getHeight() / 2);

        touchingFromTopSide = fireballs.get(fireballCounter).fireballY +
                (fireballs.get(fireballCounter).getFireballHeight() / 2) >= (bats.get(mobIndex).batY -
                bats.get(mobIndex).getHeight() / 2);


        if (touchingFromBottomSide == true && touchingFromLeftSide == true &&
                touchingFromRightSide == true && touchingFromTopSide == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Create a hit box by comparing position of two objects in relation
     * to each other
     * <p>
     * start with the fireball x and add 1/2 the width of the fireball to
     * see if it touches the (ghost x and 1/2 of the ghost width)
     *
     * @param fireballCounter
     * @param mobIndex
     * @return
     */
    private boolean isGhostCollision(int fireballCounter, int mobIndex)
    {
        boolean touchingFromLeftSide = false;
        boolean touchingFromRightSide = false;
        boolean touchingFromTopSide = false;
        boolean touchingFromBottomSide = false;

        touchingFromLeftSide = fireballs.get(fireballCounter).fireballX +
                (fireballs.get(fireballCounter).getFireballWidth() / 2) >= ghosts.get(mobIndex).ghostX -
                (ghosts.get(mobIndex).getWidth() / 2);

        touchingFromRightSide = (fireballs.get(fireballCounter).fireballX -
                fireballs.get(fireballCounter).getFireballWidth() / 2) <= (ghosts.get(mobIndex).ghostX +
                ghosts.get(mobIndex).getWidth() / 2);

        touchingFromBottomSide = fireballs.get(fireballCounter).fireballY -
                (fireballs.get(fireballCounter).getFireballHeight() / 2) <= ghosts.get(mobIndex).ghostY +
                (ghosts.get(mobIndex).getHeight() / 2);

        touchingFromTopSide = fireballs.get(fireballCounter).fireballY +
                (fireballs.get(fireballCounter).getFireballHeight() / 2) >= (ghosts.get(mobIndex).ghostY -
                ghosts.get(mobIndex).getHeight() / 2);

        if (touchingFromBottomSide == true && touchingFromLeftSide == true &&
                touchingFromRightSide == true && touchingFromTopSide == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * todo: mobs drop bombs on hunter**
     * player health changes how much of rectangle to show
     *
     * @param canvas
     */

//    todo: button to X out of the game
//    private void drawHealth(Canvas canvas)
  //  {
  //      canvas.drawRect(this.displayWidth - 180, 10, this.displayWidth - 110 +
   //             (10 * playerHealth), TEXT_SIZE, healthPaint);
  //  }

     private void drawExitButton(Canvas canvas)
     {
         exitButton.buttonX = 430;
         exitButton.buttonY = 70;

        canvas.drawBitmap(exitButton.getBitmap(),this.displayWidth - 100, 20, null);
    }

//
    private void drawScore(Canvas canvas)
    {
//        Log.i("drawScore", "Updating player score" + this.batScore);

        this.totalScore = ((this.batScore * 2) + this.ghostScore);

        canvas.drawText("Score: " + totalScore, 0, TEXT_SIZE,
                this.scorePaint);
    }

    private void drawCountDownTimer(Canvas canvas)
    {
        this.timerString = this.timerTextView.getText().toString();

        canvas.drawText("Timer: " + this.timerString, 0, 120, this.timerPaint);

        if (this.timerString == "0")
        {
            Intent intent = new Intent(this.context, GameOver.class);

            intent.putExtra("score", this.totalScore);

            this.context.startActivity(intent);
        }
    }

    /**
     * @param context
     * @param ghosts
     * @param explosions
     * @param mobIndex   index in collection
     */

    private static void doExplosionForGhosts(Context context, ArrayList<Ghost> ghosts,
                                             ArrayList<Explosion> explosions, int mobIndex)
    {
        Log.i("doExplosionForGhosts", "Have an explosion for a ghost");

        Explosion explosion = new Explosion(context);

        explosion.explosionX = ghosts.get(mobIndex).ghostX + ghosts.get(mobIndex).getWidth() / 2 -
                explosion.getExplosionWidth() / 2;

        explosion.explosionY = ghosts.get(mobIndex).ghostY + ghosts.get(mobIndex).getHeight() / 2 -
                explosion.getExplosionHeight() / 2;

        explosions.add(explosion);
    }

    /**
     * @param mobIndex   index in collection
     * @param context
     * @param bats
     * @param explosions
     */
    private static void doExplosionForBats(int mobIndex, Context context, ArrayList<Bat> bats,
                                           ArrayList<Explosion> explosions)
    {
        Explosion explosion = new Explosion(context);

        explosion.explosionX = bats.get(mobIndex).batX + bats.get(mobIndex).getWidth() / 2 -
                explosion.getExplosionWidth() / 2;

        explosion.explosionY = bats.get(mobIndex).batY + bats.get(mobIndex).getHeight() / 2 -
                explosion.getExplosionHeight() / 2;

        explosions.add(explosion);
    }

    private void drawExplosions(Canvas canvas)
    {
        for (int explosionCounter = 0; explosionCounter < explosions.size(); explosionCounter++)
        {
            canvas.drawBitmap(explosions.get(explosionCounter).getExplosion(explosions
                            .get(explosionCounter).explosionFrameNumber),
                    explosions.get(explosionCounter).explosionX, explosions
                            .get(explosionCounter).explosionY, null);

            explosions.get(explosionCounter).explosionFrameNumber++;

            // there are only 9 images
            if (explosions.get(explosionCounter).explosionFrameNumber > 8)
            {
                explosions.remove(explosionCounter);
            }
        }
    }

    private void playScoreSound()
    {
        if (scoreSound != 0)
        {
            this.soundPool.play(scoreSound, 1, 1, 0, 0, 1);
        }
    }

    private void drawDirectionArrows(Canvas canvas)
    {
        canvas.drawBitmap(leftArrow.getBitmap(), (displayWidth / 4 - leftArrow.getWidth() / 2),
                displayHeight - leftArrow.getHeight(), null);

        canvas.drawBitmap(rightArrow.getBitmap(), ((displayWidth / 4) * 3 - rightArrow.getWidth() / 2),
                displayHeight - rightArrow.getHeight(), null);
    }

    /**
     * draw the hunter at the bottom of the screen
     * <p>
     * Full Screen Needed:
     * Remember, the canvas is less than the full screen because
     * it doesn't include the notification bar at the top
     * Need to implement full screen to avoid items at bottom
     * getting cut off
     * <p>
     * displayHeight: 1794
     * hunterHeight: 525
     */

    private void drawHunter(Canvas canvas)
    {
//        Log.i("drawHunter", "screen display width: " + displayWidth +
//                ".  screen display height: " + displayHeight + ".  hunter height: " +
//                hunter.hunterHeight + ".  hunter X: " + hunter.hunterX);

        canvas.drawBitmap(hunter.getBitmap(), hunter.hunterX,
                displayHeight - hunter.hunterHeight - leftArrow.getHeight(), null);
    }

    private void drawBats(Canvas canvas)
    {
        for (int batCounter = 0; batCounter < bats.size(); batCounter++)
        {
            canvas.drawBitmap(bats.get(batCounter).getBitmap(), bats.get(batCounter).batX,
                    bats.get(batCounter).batY, null);

            bats.get(batCounter).batFrameNumber++;

            if (bats.get(batCounter).batFrameNumber > 8)
            {
                bats.get(batCounter).batFrameNumber = 0;
            }

            // move bat towards the left side of the screen
            bats.get(batCounter).batX -= bats.get(batCounter).batVelocity;


//        when bat goes off the left side of the screen
//        randomly change position and velocity of "new" bat
            if (bats.get(batCounter).batX < -bats.get(batCounter).getWidth())
            {
                bats.get(batCounter).resetPosition();
            }
        }
    }

    private void drawGhosts(Canvas canvas)
    {
        for (int ghostCounter = 0; ghostCounter < ghosts.size(); ghostCounter++)
        {
            canvas.drawBitmap(ghosts.get(ghostCounter).getBitmap(), ghosts.get(ghostCounter).ghostX,
                    ghosts.get(ghostCounter).ghostY, null);

            ghosts.get(ghostCounter).ghostFrameNumber++;

            if (ghosts.get(ghostCounter).ghostFrameNumber > 11)
            {
                ghosts.get(ghostCounter).ghostFrameNumber = 0;
            }

            // move ghost towards the left side of the screen
            ghosts.get(ghostCounter).ghostX -= ghosts.get(ghostCounter).ghostVelocity;


//        when ghost goes off the left side of the screen
//        randomly change position and velocity of "new" ghost
            if (ghosts.get(ghostCounter).ghostX < -ghosts.get(ghostCounter).getWidth())
            {
                ghosts.get(ghostCounter).resetPosition();
            }
        }
    }
}