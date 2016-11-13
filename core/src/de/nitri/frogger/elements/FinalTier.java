package de.nitri.frogger.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.math.Rectangle;

import de.nitri.frogger.Game;
import de.nitri.frogger.data.GameData;
import de.nitri.frogger.data.Sounds;
import de.nitri.frogger.screens.GameScreen;


public class FinalTier extends Tier {

    private List<Target> _targets;
    private List<Target> _flies;
    private List<Target> _crocs;
    private List<Target> _bonus200;
    private List<Target> _bonus400;

    private int _bonusCnt;
    private Random _random;
    private int selIndex;
    private GameScreen _screen;

    public FinalTier(Game game, int index) {

        super(game, index);

        _bonusCnt = 0;
        _random = new Random();
        _screen = (GameScreen) _game.screen;

    }

    @Override
    public boolean checkCollision(Player player) {

        Target sprite;
        boolean collision = false;
        Rectangle player_rec = player.bounds();
        Rectangle sprite_rec;
        player.tierSpeed = 0;
        selIndex = -1;

        int len = _targets.size();
        for (int i = 0; i < len; i++) {
            sprite = _targets.get(i);
            sprite_rec = sprite.bounds();
            if (sprite_rec == null) continue;
            //check intersects
            if (sprite_rec.overlaps(player_rec)) {
                collision = true;
                selIndex = i;
                break;
            }
        }

        Target fly = _flies.get(selIndex);
        Target croc = _crocs.get(selIndex);
        Target target = _targets.get(selIndex);
        Target bonus200 = _bonus200.get(selIndex);
        Target bonus400 = _bonus400.get(selIndex);

        if (collision) {
            //if this target has been reached already...
            if (target.visible) {
                //send player back to beginning
                player.reset();
                return false;
            } else {
                player.active = false;
                player.visible = false;
                //check if croc head is in the slot
                if (croc.visible) {
                    //kill player!!!
                    return true;
                } else {

                    int bonus = 0;
                    //check if there are flies in this slot
                    if (fly.visible) {
                        fly.visible = false;
                        bonus += GameData.POINTS_FLY;
                    }
                    if (player.hasBonus) bonus += GameData.POINTS_BONUS;

                    //show bonus points!!!
                    if (bonus > 0) {
                        if (bonus > GameData.POINTS_BONUS) {
                            bonus400.visible = true;
                        } else {
                            bonus200.visible = true;
                        }
                        _game.gameData.score += bonus;
                        //show target reached icon after displaying bonus for some time
                        final Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                _bonus400.get(selIndex).visible = false;
                                _bonus200.get(selIndex).visible = false;
                                _targets.get(selIndex).visible = true;
                                timer.cancel();
                            }
                        }, 300, 1);

                    } else {
                        target.visible = true;
                    }

                    _game.gameData.targetsReached++;
                    if (_game.gameData.sound)
                        Sounds.play(Sounds.pickup);

                    final Timer timer2 = new Timer();
                    timer2.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            _screen.targetReached();
                            if (_game.gameData.targetsReached == 5) {
                                if (_game.gameData.sound)
                                    Sounds.play(Sounds.target);
                                //start new level
                                _screen.newLevel();
                                hide();
                            }
                            timer2.cancel();
                        }
                    }, 1000, 1);


                    //add points for reaching a target
                    _game.gameData.score += GameData.POINTS_TARGET;
                    player.hide();

                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(float dt) {

        //show fly or croc head
        int len = _crocs.size();
        Target croc;
        Target target;
        for (int i = 0; i < len; i++) {
            croc = _crocs.get(i);
            if (croc.visible) {
                if (_targets.get(i).visible) {
                    croc.visible = false;
                } else {
                    target = _targets.get(i);
                    if (croc.x < target.x) {
                        croc.x += 0.4;
                    }
                }
            }
        }

        if (_bonusCnt > 80) {
            _bonusCnt = 0;
            if (_random.nextInt(10) > 6) {
                //pick an index
                final int index = _random.nextInt(_targets.size());

                if (!_targets.get(index).visible &&
                        !_flies.get(index).visible && !_crocs.get(index).visible) {
                    if (_random.nextInt(10) > 6) {
                        _crocs.get(index).x -= _crocs.get(index).width;
                        _crocs.get(index).visible = true;
                    } else {
                        _flies.get(index).visible = true;
                    }
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            _crocs.get(index).visible = false;
                            _flies.get(index).visible = false;
                            timer.cancel();
                        }
                    }, 4000, 1);
                }
            }

        }
        _bonusCnt++;
    }

    @Override
    public void reset() {
        hide();
        _bonusCnt = 0;
    }

    @Override
    public void hide() {
        for (int i = 0; i < _targets.size(); i++) {
            _targets.get(i).visible = false;
            _flies.get(i).visible = false;
            _crocs.get(i).visible = false;
            _bonus200.get(i).visible = false;
            _bonus400.get(i).visible = false;

        }
    }

    @Override
    protected void createElements() {

        _targets = new ArrayList<Target>();
        _flies = new ArrayList<Target>();
        _crocs = new ArrayList<Target>();
        _bonus200 = new ArrayList<Target>();
        _bonus400 = new ArrayList<Target>();

        Target sprite;


        float[] element_x = {
                _game.screenWidth * 0.07f,
                _game.screenWidth * 0.29f,
                _game.screenWidth * 0.5f,
                _game.screenWidth * 0.715f,
                _game.screenWidth * 0.93f
        };

        for (int i = 0; i < element_x.length; i++) {

            sprite = new Target(_game, element_x[i], _game.screenHeight - TIER_ELEMENT_Y[_index], Target.FLY);
            _flies.add(sprite);

            sprite = new Target(_game, element_x[i], _game.screenHeight - TIER_ELEMENT_Y[_index], Target.CROC);
            _crocs.add(sprite);

            sprite = new Target(_game, element_x[i], _game.screenHeight - TIER_ELEMENT_Y[_index], Target.TARGET);
            _targets.add(sprite);

            //TODO: test
            //if (i < 4) sprite.visible = true;


            sprite = new Target(_game, element_x[i], _game.screenHeight - TIER_ELEMENT_Y[_index], Target.BONUS_200);
            _bonus200.add(sprite);

            sprite = new Target(_game, element_x[i], _game.screenHeight - TIER_ELEMENT_Y[_index], Target.BONUS_400);
            _bonus400.add(sprite);


        }
    }
}
