package kalah.controller;

import kalah.controller.exception.InvalidActionException;

public interface IPlayerAction {
    void execute() throws InvalidActionException;
}
