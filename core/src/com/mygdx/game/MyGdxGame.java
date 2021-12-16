package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame extends ApplicationAdapter {
	private Texture dropImage;
	private Texture bucketImage;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Rectangle bucket;
	private Array<Rectangle> raindrops;
	private long lastDropTime;

   Texture background;

	private int score;
	private String yourScoreName;
	BitmapFont yourBitmapFontName;

	private float timeSeconds = 0f;
	private float period = 1f;
	private int timer;
	private int timerX = 0;
	private int timerY = 0;

	@Override
	public void create() {

		background = new Texture(Gdx.files.internal("winterBG.jpg"));

		dropImage = new Texture(Gdx.files.internal("winter.png"));
		bucketImage = new Texture(Gdx.files.internal("skaterKitty.png"));


		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 60;
		bucket.height = 60;


		raindrops = new Array<Rectangle>();
		spawnRaindrop();

		score = 0;
		yourScoreName = "score: 0";
		yourBitmapFontName = new BitmapFont();

		//timer = 0;

	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render() {


		ScreenUtils.clear(1, 0, 1, 1);


		camera.update();

		batch.setProjectionMatrix(camera.combined);


		batch.begin();

		batch.draw(background,0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(bucketImage, bucket.x, bucket.y);
		batch.draw(bucketImage, bucket.x, bucket.y);

		//yourBitmapFontName.setColor(6, 6, 6, 1);
		yourBitmapFontName.draw(batch, yourScoreName, 25, 450);

	/*	timeSeconds +=Gdx.graphics.getRawDeltaTime();
		if(timeSeconds > period){
			timeSeconds-=period;
			timer ++;
		}*/

		yourBitmapFontName.draw(batch, "timer:" + timer, timerX, timerY);



		for(Rectangle raindrop: raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();


		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			bucketImage = new Texture(Gdx.files.internal("skaterKittyLeft.png"));
			bucket.x -= 500 * Gdx.graphics.getDeltaTime();
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			bucketImage = new Texture(Gdx.files.internal("skaterKitty.png"));
			bucket.x += 500 * Gdx.graphics.getDeltaTime();
		}



		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;


		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();


		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) iter.remove();


			if(raindrop.overlaps(bucket)) {
				score ++;
				yourScoreName = "score: " + score;
				iter.remove();
			}
		}
		//System.out.println(score);
		if(score == 10) {
			background = new Texture(Gdx.files.internal("springBG.jpg"));
			dropImage = new Texture(Gdx.files.internal("spring.png"));
		}
		if(score == 20) {
			background = new Texture(Gdx.files.internal("summerBG.jpg"));
			dropImage = new Texture(Gdx.files.internal("summer.png"));
		}
		if(score == 30) {
			background = new Texture(Gdx.files.internal("automnBG.jpg"));
			dropImage = new Texture(Gdx.files.internal("automn.png"));
		}
		if(score >= 40) {
			background = new Texture(Gdx.files.internal("galaxyBG.jpg"));
			dropImage = new Texture(Gdx.files.internal("star.png"));

			timerX = 25;
			timerY = 420;
			timeSeconds +=Gdx.graphics.getRawDeltaTime();
			if(timeSeconds > period) {
				timeSeconds -= period;
				timer++;
			}
			if (timer == 30){
				background = null;
			}

			if(Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= 600 * Gdx.graphics.getDeltaTime();
			if(Gdx.input.isKeyPressed(Keys.RIGHT)) bucket.x += 600 * Gdx.graphics.getDeltaTime();

			for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
				Rectangle raindrop = iter.next();
				raindrop.y -=205 * Gdx.graphics.getDeltaTime();
				if (raindrop.y + 64 < 0) iter.remove();
			}
			if(TimeUtils.nanoTime() - lastDropTime > 99999990) spawnRaindrop();

		}


	}

	@Override
	public void dispose() {
		dropImage.dispose();
		bucketImage.dispose();
		batch.dispose();
	}
}